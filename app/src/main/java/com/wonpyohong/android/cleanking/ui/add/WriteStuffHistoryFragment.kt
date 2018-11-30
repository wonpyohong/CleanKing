package com.wonpyohong.android.cleanking.ui.add

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseFragment
import com.wonpyohong.android.cleanking.databinding.FragmentWriteStuffHistoryBinding
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.room.stuff.StuffDatabase
import com.wonpyohong.android.cleanking.room.stuff.StuffHistory
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import com.wonpyohong.android.cleanking.support.recyclerview.DragHelperCallback
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime

@BindingAdapter("bind:categoryItem")
fun bindCategoryItem(recyclerView: RecyclerView, categoryList: ObservableArrayList<Category>) {
    viewModel.categoryList = categoryList

    val adapter = recyclerView.adapter as CategoryAdapter
    adapter.setItem(categoryList)
}

@BindingAdapter("bind:stuffItem")
fun bindStuffItem(recyclerView: RecyclerView, stuffList: ObservableArrayList<Stuff>) {
    viewModel.stuffList = stuffList

    val adapter = recyclerView.adapter as StuffAdapter
    adapter.setItem(stuffList)
}

val viewModel = WriteStuffHistoryViewModel()

class WriteStuffHistoryFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_write_stuff_history

    lateinit var binding: FragmentWriteStuffHistoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        binding.viewModel = viewModel
        binding.writeStuffHistoryFragment = this
        initCategoryRecyclerView()
        initStuffRecyclerView()
    }

    private fun initCategoryRecyclerView() {
        val categoryAdapter = CategoryAdapter(viewModel)
        initRecyclerViewCommon(binding.categoryRecyclerView, categoryAdapter)

        StuffDatabase.getInstance().getCategoryDao().getAllCategoryList().subscribe {
            viewModel.categoryList.clear()
            viewModel.categoryList.addAll(it)
        }
    }

    private fun initStuffRecyclerView() {
        val stuffAdapter = StuffAdapter(viewModel)
        initRecyclerViewCommon(binding.stuffRecyclerView, stuffAdapter)

        viewModel.selectedCategory.addOnPropertyChanged {selectedObservableCategory ->
            StuffDatabase.getInstance().getStuffDao().getStuffList(selectedObservableCategory.get()!!.categoryId)
                .subscribe {list ->
                    viewModel.stuffList.clear()
                    viewModel.stuffList.addAll(list)
                } }

    }

    private fun initRecyclerViewCommon(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = ChipsLayoutManager.newBuilder(context!!).build()
        recyclerView.addItemDecoration(
            SpacingItemDecoration(20, 20)
        )

        val touchHelper = ItemTouchHelper(DragHelperCallback(adapter as ItemTouchHelperAdapter))
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.write_history, menu)
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_write_history) {
            return writeHistory()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun writeHistory(): Boolean {
        if (viewModel.selectedCategory.get() == null) {
            AlertDialog.Builder(context!!)
                .setMessage("카테고리를 선택하셔야 합니다")
                .show()

            return true
        }

        if (viewModel.selectedStuff.get() == null) {
            AlertDialog.Builder(context!!)
                .setMessage("세부 항목을 선택하셔야 합니다")
                .show()

            return true
        }

        val stuff = StuffHistory(0, LocalDateTime.now().minusHours(4).toLocalDate().toString(),
            viewModel.selectedCategory.get()!!.categoryName,
            viewModel.selectedStuff.get()!!.stuffName)

        Single.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                StuffDatabase.getInstance().getStuffHistoryDao().insert(stuff)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ exerciseId ->
                RxDayDataSetChangedEvent.sendEvent(RxDayDataSetChangedEvent.DayDataSetChanged())

                activity?.finish()
            }, {
                it.printStackTrace()
            })

        return true
    }

    fun addStuff() {
        viewModel.selectedCategory.get()?.let {
            val newStuff = Stuff(
                0,
                it.categoryId,
                binding.newStuffEditText.text.toString()            // two-way binding이 필요할 듯
            )

            Completable.fromAction {
                StuffDatabase.getInstance().getStuffDao().insert(newStuff)
            }.subscribeOn(Schedulers.io())
                .subscribe()

        } ?:
        AlertDialog.Builder(context!!)              // event 로 notify 해야 한다 Rx?
            .setMessage("카테고리를 선택하셔야 합니다")
            .show()
    }

    fun <T: android.databinding.Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
        addOnPropertyChangedCallback(
            object: android.databinding.Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(
                    observable: android.databinding.Observable?, i: Int) =
                    callback(observable as T)
            })

}