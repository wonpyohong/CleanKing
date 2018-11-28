package com.wonpyohong.android.cleanking.ui.add

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.databinding.ItemStuffBinding
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import java.util.*


class StuffAdapter:
        RecyclerView.Adapter<StuffAdapter.StuffViewHolder>(), ItemTouchHelperAdapter {

    lateinit var stuffList: List<Stuff>
    var selectedStuff: ObservableField<Stuff> = ObservableField()

    fun setItem(stuffList: List<Stuff>?) {
        if (stuffList != null) {
            this.stuffList = stuffList
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffViewHolder {
        val binding = ItemStuffBinding.inflate(LayoutInflater.from(parent.context))
        return StuffViewHolder(binding)
    }

    override fun getItemId(position: Int) = stuffList[position].hashCode().toLong()

    override fun getItemCount() = stuffList.size

    override fun onBindViewHolder(holder: StuffViewHolder, position: Int) {
        holder.bind(stuffList[position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(stuffList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(stuffList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
    }

    fun onStuffClicked(stuff: Stuff) {
        stuffList.forEach {
            it.setSelectedFalse()
        }
        stuff.onClicked()
        selectedStuff.set(stuff)
    }

    inner class StuffViewHolder(val binding: ItemStuffBinding) :
            RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        var selected = ObservableBoolean(false)

        fun bind(stuff: Stuff) {
            binding.stuff = stuff
            binding.stuffAdapter = this@StuffAdapter
        }

        override fun onItemSelected() {
            selected.set(true)
        }

        override fun onItemCleared() {
            selected.set(false)
        }
    }
}