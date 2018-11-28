package com.wonpyohong.android.cleanking.calendar.day

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.calendar.month.MAX_DATE
import com.wonpyohong.android.cleanking.calendar.month.MIN_DATE
import com.wonpyohong.android.cleanking.room.stuff.StuffDatabase
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import com.wonpyohong.android.cleanking.support.recyclerview.DragHelperCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_day.*
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit


class DayAdapter(private val context: Context): RecyclerView.Adapter<DayAdapter.DayViewHolder>() {
    var compositeDisposable = CompositeDisposable()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        compositeDisposable.add(RxDayDataSetChangedEvent.events.subscribe {
            notifyDataSetChanged()
        })
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        compositeDisposable.dispose()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false)
        return DayViewHolder(itemView)
    }

    override fun getItemCount() = ChronoUnit.DAYS.between(MIN_DATE, MAX_DATE).toInt()

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        holder.bind(MIN_DATE.plusDays(position.toLong()))
    }

    inner class DayViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(date: LocalDate) {
            dumpRecyclerView.isNestedScrollingEnabled = false
            dumpRecyclerView.layoutManager = LinearLayoutManager(context)

            compositeDisposable.add(
                StuffDatabase.getInstance().getStuffHistoryDao().getDayStuffList(date.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {stuffList ->
                    val dumpAdapter = DayStuffHistoryAdapter(context, stuffList)
                    dumpRecyclerView.adapter = dumpAdapter

                    val touchHelper = ItemTouchHelper(DragHelperCallback(dumpAdapter))
                    touchHelper.attachToRecyclerView(dumpRecyclerView)
                })
        }
    }
}