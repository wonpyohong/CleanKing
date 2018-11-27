package com.wonpyohong.android.cleanking.ui.add

import android.annotation.SuppressLint
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.wonpyohong.android.cleanking.room.dump.Dump
import com.homedev.android.dietapp.room.exercise.DumpDatabase
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseFragment
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import com.wonpyohong.android.cleanking.support.recyclerview.DragHelperCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_dump.*
import org.threeten.bp.LocalDate
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.wonpyohong.android.cleanking.databinding.FragmentAddDumpBinding
import com.wonpyohong.android.cleanking.room.category.Category

@BindingAdapter("bind:item")
fun bindItem(recyclerView: RecyclerView, categoryList: ObservableArrayList<Category>) {
    val adapter = recyclerView.adapter as CategoryAdapter
    adapter.setItem(categoryList)
}

class AddDumpFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_add_dump

    var selectedCategory: String? = null

    lateinit var binding: FragmentAddDumpBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        initCategoryTag()
    }

    private fun initCategoryTag() {
        val categoryAdapter = CategoryAdapter()
        categoryAdapter.setHasStableIds(true)
        binding.categoryRecyclerView.adapter = categoryAdapter

        val categoryList = ObservableArrayList<Category>()
        binding.categoryList = categoryList
        binding.categoryAdapter = categoryAdapter

        DumpDatabase.getInstance().getCategoryDao().getAllCategoryList().subscribe {
            categoryList.clear()
            categoryList.addAll(it)
        }

        binding.categoryRecyclerView.layoutManager = ChipsLayoutManager.newBuilder(context!!).build()
        binding.categoryRecyclerView.addItemDecoration(
            SpacingItemDecoration(20, 20)
        )

        val touchHelper = ItemTouchHelper(DragHelperCallback(categoryAdapter))
        touchHelper.attachToRecyclerView(categoryRecyclerView)
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
        if (selectedCategory == null) {
            AlertDialog.Builder(context!!)
                .setMessage("카테고리를 선택하셔야 합니다")
                .show()

            return true
        }

        val dump = Dump(0, LocalDate.now().toString(), selectedCategory!!, binding.dumpName.text.toString(), reason.text.toString())

        Single.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                DumpDatabase.getInstance().getDumpDao().insert(dump)
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