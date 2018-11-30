package com.wonpyohong.android.cleanking.ui.add

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseFragment
import com.wonpyohong.android.cleanking.databinding.FragmentWriteStuffHistoryBinding
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.room.stuff.Stuff

@BindingAdapter("bind:categoryItem")
fun bindCategoryItem(recyclerView: RecyclerView, categoryList: ObservableArrayList<Category>) {
    viewModel.categoryList = categoryList
    recyclerView.adapter.notifyDataSetChanged()
}

@BindingAdapter("bind:stuffItem")
fun bindStuffItem(recyclerView: RecyclerView, stuffList: ObservableArrayList<Stuff>) {
    viewModel.stuffList = stuffList
    recyclerView.adapter.notifyDataSetChanged()
}

val viewModel = WriteStuffHistoryViewModel()

class WriteStuffHistoryFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_write_stuff_history

    private lateinit var binding: FragmentWriteStuffHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvents()
    }

    private fun observeEvents() {
        viewModel.getActionSignal().observe(this, Observer {
            when (it is WriteStuffHistoryViewModel.ACTION) {
                it == WriteStuffHistoryViewModel.ACTION.CATEGORY_NOT_SELECTED -> {
                    AlertDialog.Builder(context!!)
                        .setMessage("카테고리를 선택하셔야 합니다")
                        .show()
                }
                it == WriteStuffHistoryViewModel.ACTION.STUFF_NOT_SELECTED -> {
                    AlertDialog.Builder(context!!)
                        .setMessage("세부 항목을 선택하셔야 합니다")
                        .show()
                }
                it == WriteStuffHistoryViewModel.ACTION.FINISH -> {
                    activity?.finish()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        binding.viewModel = viewModel

        viewModel.initCategoryRecyclerView(binding.categoryRecyclerView, ChipsLayoutManager.newBuilder(context!!).build())
        viewModel.initStuffRecyclerView(binding.stuffRecyclerView, ChipsLayoutManager.newBuilder(context!!).build())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.write_history, menu)
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_write_history) {
            return viewModel.writeHistory()
        }

        return super.onOptionsItemSelected(item)
    }
}