package com.wonpyohong.android.cleanking.calendar.day

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.room.dump.Dump
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.databinding.ItemDumpBinding
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class DumpAdapter(private val context: Context, val dumpList: List<Dump>):
        RecyclerView.Adapter<DumpAdapter.DumpViewHolder>(),
        ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DumpViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_dump, parent, false)
        return DumpViewHolder(itemView)
    }

    override fun getItemCount() = dumpList.size

    override fun onBindViewHolder(holder: DumpViewHolder, position: Int) {
        holder.bind(dumpList[position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(dumpList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(dumpList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class DumpViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer, ItemTouchHelperViewHolder {

        var selected = ObservableBoolean(false)

        fun bind(dump: Dump) {
            val viewBinding = DataBindingUtil.bind<ItemDumpBinding>(containerView)
            viewBinding?.dump = dump
            viewBinding?.dumpViewHolder = this
        }

        override fun onItemSelected() {
            selected.set(true)
        }

        override fun onItemCleared() {
            selected.set(false)
        }
    }
}