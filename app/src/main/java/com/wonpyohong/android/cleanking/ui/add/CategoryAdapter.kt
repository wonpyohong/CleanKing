package com.wonpyohong.android.cleanking.ui.add

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.databinding.ItemCategoryBinding
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import java.util.*


class CategoryAdapter:
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), ItemTouchHelperAdapter {

    lateinit var categoryList: List<Category>
    var selectedCategory: ObservableField<Category> = ObservableField()

    fun setItem(categoryList: List<Category>?) {
        if (categoryList != null) {
            this.categoryList = categoryList
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context))
        return CategoryViewHolder(binding)
    }

    override fun getItemId(position: Int) = categoryList[position].hashCode().toLong()

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(categoryList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(categoryList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
    }

    fun onCategoryClicked(category: Category) {
        categoryList.forEach {
            it.setSelectedFalse()
        }
        category.onClicked()
        selectedCategory.set(category)
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
            RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        var selected = ObservableBoolean(false)

        fun bind(category: Category) {
            binding.category = category
            binding.categoryAdapter = this@CategoryAdapter
        }

        override fun onItemSelected() {
            selected.set(true)
        }

        override fun onItemCleared() {
            selected.set(false)
        }
    }
}