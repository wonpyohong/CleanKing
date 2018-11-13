package com.wonpyohong.android.cleanking.calendar.month

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonpyohong.android.cleanking.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_month.*
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

val MIN_DATE = LocalDate.now().minusMonths(100).withDayOfMonth(1)
val MAX_DATE = LocalDate.now().plusMonths(100).withDayOfMonth(1)

class MonthAdapter(private val context: Context): RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false)
        return MonthAdapter.MonthViewHolder(itemView)
    }

    override fun getItemCount() = ChronoUnit.MONTHS.between(MIN_DATE, MAX_DATE).toInt()

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.bind(MIN_DATE.plusMonths(position.toLong()))
    }

    class MonthViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(date: LocalDate) {
            monthView.firstDate = date
        }
    }
}