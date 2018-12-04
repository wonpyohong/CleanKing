package com.wonpyohong.android.cleanking.ui.add

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
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
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


@BindingAdapter("bind:categoryItem")
fun bindCategoryItem(recyclerView: RecyclerView, categoryList: LiveData<List<Category>>) {
    recyclerView.adapter.notifyDataSetChanged()
}

@BindingAdapter("bind:stuffItem")
fun bindStuffItem(recyclerView: RecyclerView, stuffList: LiveData<MutableList<Stuff>>) {
    stuffList.value?.sortWith(compareByDescending { it.frequency })
    recyclerView.adapter.notifyDataSetChanged()
}

class WriteStuffHistoryFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_write_stuff_history

    private lateinit var binding: FragmentWriteStuffHistoryBinding

    private val viewModel: WriteStuffHistoryViewModel by viewModel()
    private val categoryAdapter: CategoryAdapter by inject { parametersOf(this) }
    private val stuffAdapter: StuffAdapter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.observeCategoryList()
        viewModel.observeStuffListOnSelectedCategory()
        observeEvents()
        observeMergeStuffEvents()
    }

    private fun observeEvents() {
        viewModel.getActionSignal().observe(this, Observer {
            when (it is WriteStuffHistoryViewModel.ACTION) {
                it == WriteStuffHistoryViewModel.ACTION.CATEGORY_NOT_SELECTED -> {
                    AlertDialog.Builder(context!!)
                        .setMessage("카테고리를 선택하셔야 합니다")
                        .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                        .show()
                }

                it == WriteStuffHistoryViewModel.ACTION.STUFF_NOT_SELECTED -> {
                    AlertDialog.Builder(context!!)
                        .setMessage("세부 항목을 선택하셔야 합니다")
                        .setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
                        .show()
                }

                it == WriteStuffHistoryViewModel.ACTION.FINISH -> {
                    activity?.finish()
                }
            }
        })
    }

    private fun observeMergeStuffEvents() {
        viewModel.getMergeStuffSignal().observe(this, Observer {
            AlertDialog.Builder(context!!)
                .setMessage("통합하시겠습니까?")
                .setPositiveButton("확인") { dialog, which ->
                    dialog.dismiss()
                    viewModel.onMergeStuffConfirmed(it!!)
                }
                .setNegativeButton("취소") { dialog, which -> dialog.dismiss() }
                .show()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind(view)!!
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        initRecyclerViewCommon(binding.categoryRecyclerView, categoryAdapter, ChipsLayoutManager.newBuilder(context!!).build())
        initRecyclerViewCommon(binding.stuffRecyclerView, stuffAdapter, ChipsLayoutManager.newBuilder(context!!).build())
    }

    private fun initRecyclerViewCommon(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, chipsLayoutManager: ChipsLayoutManager) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = chipsLayoutManager
        recyclerView.addItemDecoration(
            SpacingItemDecoration(20, 20)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.write_history, menu)
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_write_history) {
            viewModel.writeHistory()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}