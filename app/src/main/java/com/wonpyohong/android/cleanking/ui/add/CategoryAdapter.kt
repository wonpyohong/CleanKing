package com.wonpyohong.android.cleanking.ui.add

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.databinding.ItemCategoryBinding
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import com.wonpyohong.android.cleanking.type.CategoryType
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class CategoryAdapter(private val context: Context):
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(), ItemTouchHelperAdapter {

    private val categoryList = CategoryType.values().toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(itemView)
    }

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class CategoryViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer, ItemTouchHelperViewHolder {

        var selected = ObservableBoolean(false)

        fun bind(categoryType: CategoryType) {
            val viewBinding = DataBindingUtil.bind<ItemCategoryBinding>(containerView)
            viewBinding?.categoryType = categoryType
        }

        override fun onItemSelected() {
            selected.set(true)
        }

        override fun onItemCleared() {
            selected.set(false)
        }

        fun onClick(containerView: View) {

        }
    }
}