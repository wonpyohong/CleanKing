package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.wonpyohong.android.cleanking.databinding.ItemStuffAppendBinding
import com.wonpyohong.android.cleanking.databinding.ItemStuffNormalBinding
import com.wonpyohong.android.cleanking.hideKeyboard
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.showKeyboard
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperViewHolder
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.standalone.KoinComponent
import java.util.*


class StuffAdapter(val lifeCycleOwner: LifecycleOwner):
        RecyclerView.Adapter<StuffAdapter.StuffViewHolder>(), ItemTouchHelperAdapter, KoinComponent {

    val viewModel: WriteStuffHistoryViewModel by viewModel(lifeCycleOwner)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StuffViewHolder {
        return if (viewType == ViewType.APPEND.ordinal) {
            val binding = ItemStuffAppendBinding.inflate(LayoutInflater.from(parent.context))
            StuffAppendViewHolder(binding)
        } else {
            val binding = ItemStuffNormalBinding.inflate(LayoutInflater.from(parent.context))
            StuffNormalViewHolder(binding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position in 0 until (viewModel.stuffList.value?.size ?: 0)) {
            ViewType.NORMAL.ordinal
        } else {
            ViewType.APPEND.ordinal
        }
    }

    override fun getItemId(position: Int): Long {
        return if (position in 0 until (viewModel.stuffList.value?.size ?: 0)) {
            viewModel.stuffList.value!![position].hashCode().toLong()
        } else {
            -1
        }
    }

    override fun getItemCount() = (viewModel.stuffList.value?.size ?: 0) + 1

    override fun onBindViewHolder(holder: StuffViewHolder, position: Int) {
        if (position in 0 until (viewModel.stuffList.value?.size ?: 0)) {
            holder.bind(viewModel.stuffList.value!![position])
        } else {
            holder.bind(null)
        }

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

    abstract inner class StuffViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(stuff: Stuff?)
    }

    inner class StuffNormalViewHolder(private val binding: ItemStuffNormalBinding) : StuffViewHolder(binding) {
        override fun bind(stuff: Stuff?) {
            binding.stuff = stuff
            binding.viewModel = viewModel
            binding.setLifecycleOwner(lifeCycleOwner)
        }
    }

    inner class StuffAppendViewHolder(private val binding: ItemStuffAppendBinding) : StuffViewHolder(binding), ItemTouchHelperViewHolder {
        override fun onItemSelected() {
        }

        override fun onItemCleared() {
        }

        override fun bind(stuff: Stuff?) {
            binding.viewModel = viewModel
            binding.setLifecycleOwner(lifeCycleOwner)
            binding.newStuffEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                view as EditText

                if (hasFocus) {
                    Log.d("HWP", "has focus")
                    view.setText("")
                    showKeyboard(view)
                } else {
                    if (view.text.isNotEmpty()) {
                        viewModel.addStuff()
                        view.setText("")
                    }

                    hideKeyboard(view)
                }
            }
        }
    }

    enum class ViewType {
        APPEND, NORMAL
    }
}