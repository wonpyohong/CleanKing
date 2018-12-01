package com.wonpyohong.android.cleanking.ui.add

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
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
import com.wonpyohong.android.cleanking.support.recyclerview.DragHelperCallback
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import org.koin.android.viewmodel.ext.android.viewModel

@BindingAdapter("bind:categoryItem")
fun bindCategoryItem(recyclerView: RecyclerView, categoryList: LiveData<List<Category>>) {
    recyclerView.adapter.notifyDataSetChanged()
}

@BindingAdapter("bind:stuffItem")
fun bindStuffItem(recyclerView: RecyclerView, stuffList: LiveData<List<Stuff>>) {
    recyclerView.adapter.notifyDataSetChanged()
}

class WriteStuffHistoryFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_write_stuff_history

    private lateinit var binding: FragmentWriteStuffHistoryBinding

    private val viewModel: WriteStuffHistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.observeCategoryList()
        viewModel.observeStuffListOnSelectedCategory()
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
        binding.setLifecycleOwner(this)

        initCategoryRecyclerView(binding.categoryRecyclerView, ChipsLayoutManager.newBuilder(context!!).build())
        initStuffRecyclerView(binding.stuffRecyclerView, ChipsLayoutManager.newBuilder(context!!).build())
    }

    private fun initCategoryRecyclerView(categoryRecyclerView: RecyclerView, chipsLayoutManager: ChipsLayoutManager) {
        val categoryAdapter = CategoryAdapter(viewModel)
        initRecyclerViewCommon(categoryRecyclerView, categoryAdapter, chipsLayoutManager)
    }

    private fun initStuffRecyclerView(stuffRecyclerView: RecyclerView, chipsLayoutManager: ChipsLayoutManager) {
        val stuffAdapter = StuffAdapter(viewModel)
        initRecyclerViewCommon(stuffRecyclerView, stuffAdapter, chipsLayoutManager)
    }

    private fun initRecyclerViewCommon(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, chipsLayoutManager: ChipsLayoutManager) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = chipsLayoutManager
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
            return viewModel.writeHistory()
        }

        return super.onOptionsItemSelected(item)
    }
}