package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LifecycleOwner
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.wonpyohong.android.cleanking.databinding.ItemStuffAppendBinding
import com.wonpyohong.android.cleanking.databinding.ItemStuffNormalBinding
import com.wonpyohong.android.cleanking.hideKeyboard
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.showKeyboard
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.standalone.KoinComponent


class StuffAdapter(val lifeCycleOwner: LifecycleOwner):
        RecyclerView.Adapter<StuffAdapter.StuffViewHolder>(), KoinComponent {

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

    abstract inner class StuffViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(stuff: Stuff?)
    }

    inner class StuffNormalViewHolder(private val binding: ItemStuffNormalBinding) : StuffViewHolder(binding) {
        override fun bind(stuff: Stuff?) {
            binding.stuff = stuff!!
            binding.viewModel = viewModel
            binding.setLifecycleOwner(lifeCycleOwner)
            binding.rootLayout.setOnDragListener { destinationView, event ->
                val (draggingView, draggingStuff) = event.localState as Pair<View, Stuff>
                if (draggingStuff != stuff) {
                    when (event.action) {
                        DragEvent.ACTION_DRAG_ENTERED -> {
                            stuff.setSelectedTrue()
                        }

                        DragEvent.ACTION_DRAG_EXITED -> {
                            stuff.setSelectedFalse()
                        }

                        DragEvent.ACTION_DROP -> {
                            stuff.setSelectedFalse()
                            viewModel.requestMergeStuff(draggingStuff, stuff)
                        }

                        DragEvent.ACTION_DRAG_ENDED -> {
                            stuff.setSelectedFalse()
                            draggingView.alpha = 1f
                        }
                    }
                }
                true
            }
        }
    }

    inner class StuffAppendViewHolder(private val binding: ItemStuffAppendBinding) : StuffViewHolder(binding) {
        override fun bind(stuff: Stuff?) {
            binding.viewModel = viewModel
            binding.setLifecycleOwner(lifeCycleOwner)
            binding.newStuffEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
                view as EditText

                if (hasFocus) {
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