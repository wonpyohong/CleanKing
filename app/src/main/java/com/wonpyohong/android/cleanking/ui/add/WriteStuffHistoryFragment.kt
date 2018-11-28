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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate

@BindingAdapter("bind:categoryItem")
fun bindCategoryItem(recyclerView: RecyclerView, categoryList: ObservableArrayList<Category>) {
    val adapter = recyclerView.adapter as CategoryAdapter
    adapter.setItem(categoryList)
}

@BindingAdapter("bind:stuffItem")
fun bindStuffItem(recyclerView: RecyclerView, stuffList: ObservableArrayList<Stuff>) {
    val adapter = recyclerView.adapter as StuffAdapter
    adapter.setItem(stuffList)
}

class AddDumpFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_write_stuff_history

    var selectedStuff: Stuff? = null

    lateinit var binding: FragmentWriteStuffHistoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        initCategoryRecyclerView()
        initStuffRecyclerView()
    }

    private fun initCategoryRecyclerView() {
        val categoryAdapter = CategoryAdapter()
        initRecyclerViewCommon(binding.categoryRecyclerView, categoryAdapter)

        val categoryList = ObservableArrayList<Category>()
        binding.categoryList = categoryList
        binding.categoryAdapter = categoryAdapter

        StuffDatabase.getInstance().getCategoryDao().getAllCategoryList().subscribe {
            categoryList.clear()
            categoryList.addAll(it)
        }
    }

    private fun initStuffRecyclerView() {
        val stuffAdapter = StuffAdapter()
        initRecyclerViewCommon(binding.stuffRecyclerView, stuffAdapter)

        val stuffList = ObservableArrayList<Stuff>()
        binding.stuffList = stuffList
        binding.stuffAdapter = stuffAdapter

        StuffDatabase.getInstance().getStuffDao().getStuffList().subscribe {
            stuffList.clear()
            stuffList.addAll(it)
        }
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
        inflater.inflate(R.menu.add_dump, menu)
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            return addDump()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addDump(): Boolean {
        if (selectedStuff == null) {
            AlertDialog.Builder(context!!)
                .setMessage("카테고리를 선택하셔야 합니다")
                .show()

            return true
        }

        val stuff = StuffHistory(0, LocalDate.now().toString(), selectedStuff!!.id)

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
}