package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.databinding.ItemCategoryBinding
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.standalone.KoinComponent
import java.util.*


class CategoryAdapter(val lifeCycleOwner: LifecycleOwner):
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), ItemTouchHelperAdapter, KoinComponent {

    val viewModel: WriteStuffHistoryViewModel by viewModel(lifeCycleOwner)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(binding)
    }

    override fun getItemId(position: Int) = viewModel.categoryList.value!![position].hashCode().toLong()

    override fun getItemCount() = viewModel.categoryList.value?.size ?: 0

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(viewModel.categoryList.value!![position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(viewModel.categoryList.value, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(viewModel.categoryList.value, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.category = category
            binding.viewModel = viewModel
            binding.setLifecycleOwner(lifeCycleOwner)
        }
    }
}