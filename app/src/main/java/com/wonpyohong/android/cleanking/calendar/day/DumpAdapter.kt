package com.wonpyohong.android.cleanking.calendar.day

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.homedev.android.dietapp.room.exercise.Dump
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_dump.*
import java.util.*

class DumpAdapter(private val context: Context, val dumpList: List<Dump>):
        RecyclerView.Adapter<DumpAdapter.ExerciseTodoViewHolder>(),
        ItemTouchHelperAdapter {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseTodoViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_dump, parent, false)
        return ExerciseTodoViewHolder(itemView)
    }

    override fun getItemCount() = dumpList.size

    override fun onBindViewHolder(holder: ExerciseTodoViewHolder, position: Int) {
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

    inner class ExerciseTodoViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer, ItemTouchHelperViewHolder {
        fun bind(exerciseTodo: Dump) {
            category.text = exerciseTodo.category
            dumpName.text = exerciseTodo.dumpName
        }

        override fun onItemSelected() {
            dumpLayout.setCardBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            dumpLayout.setCardBackgroundColor(Color.WHITE)
        }
    }
}