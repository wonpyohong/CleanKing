package com.wonpyohong.android.cleanking.calendar.day

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.databinding.ItemDayStuffHistoryBinding
import com.wonpyohong.android.cleanking.room.stuff.StuffHistory
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import kotlinx.android.extensions.LayoutContainer
import java.util.*

class DayStuffHistoryAdapter(private val context: Context, val stuffHistoryList: List<StuffHistory>):
        RecyclerView.Adapter<DayStuffHistoryAdapter.DumpViewHolder>(),
        ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DumpViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_day_stuff_history, parent, false)
        return DumpViewHolder(itemView)
    }

    override fun getItemCount() = stuffHistoryList.size

    override fun onBindViewHolder(holder: DumpViewHolder, position: Int) {
        holder.bind(stuffHistoryList[position])
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(stuffHistoryList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(stuffHistoryList, i, i - 1)
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

        fun bind(stuffHistory: StuffHistory) {
            val viewBinding = DataBindingUtil.bind<ItemDayStuffHistoryBinding>(containerView)
            viewBinding?.stuffHistory = stuffHistory
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