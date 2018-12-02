package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.databinding.ItemStuffBinding
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.standalone.KoinComponent
import java.util.*


class StuffAdapter(val lifeCycleOwner: LifecycleOwner):
        RecyclerView.Adapter<StuffAdapter.StuffViewHolder>(), ItemTouchHelperAdapter, KoinComponent {

    val viewModel: WriteStuffHistoryViewModel by viewModel(lifeCycleOwner)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffViewHolder {
        val binding = ItemStuffBinding.inflate(LayoutInflater.from(parent.context))
        return StuffViewHolder(binding)
    }

    override fun getItemId(position: Int) = viewModel.stuffList.value!![position].hashCode().toLong()

    override fun getItemCount() = viewModel.stuffList.value?.size ?: 0

    override fun onBindViewHolder(holder: StuffViewHolder, position: Int) {
        holder.bind(viewModel.stuffList.value!![position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(viewModel.stuffList.value, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(viewModel.stuffList.value, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
    }

    inner class StuffViewHolder(val binding: ItemStuffBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stuff: Stuff) {
            binding.stuff = stuff
            binding.viewModel = viewModel
            binding.setLifecycleOwner(lifeCycleOwner)
        }
    }
}