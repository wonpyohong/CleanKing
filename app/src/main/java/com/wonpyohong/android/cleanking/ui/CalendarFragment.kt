package com.wonpyohong.android.cleanking.ui

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseFragment
import com.wonpyohong.android.cleanking.calendar.day.DayAdapter
import com.wonpyohong.android.cleanking.calendar.month.MIN_DATE
import com.wonpyohong.android.cleanking.calendar.month.MonthAdapter
import com.wonpyohong.android.cleanking.support.RxAnyEvent
import kotlinx.android.synthetic.main.day_bottom_sheet.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

var selectedDate: LocalDate = LocalDate.now()
    set(value) {
        RxAnyEvent.sendEvent(value)
        field = value
    }

class CalendarFragment : BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createDisposable.add(RxAnyEvent.events.subscribe { selectedDate ->
            activity?.title = "${(selectedDate as LocalDate).year}.${selectedDate.monthValue}"
            selectedDateTextView.text = "$selectedDate"
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(monthRecyclerView, MonthAdapter(context!!))
        initRecyclerView(dayRecyclerView, DayAdapter(context!!))

        goToday()
        goTodayButton.setOnClickListener { goToday() }

        fab.setOnClickListener {
            val dialog = AddDumpDialog()
            dialog.show(activity!!.supportFragmentManager!!, "addDump")
        }
    }

    private fun initRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            this.adapter = adapter

            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(this)
        }
    }

    private fun goToday() {
        selectedDate = LocalDate.now()
        goToSelectedDate()
    }

    private fun goToSelectedDate() {
        monthRecyclerView.scrollToPosition(getMonthPositionSelectedDate())
        dayRecyclerView.scrollToPosition(getDayPositionSelectedDate())
    }

    private fun getMonthPositionSelectedDate() =
        ChronoUnit.MONTHS.between(MIN_DATE, selectedDate).toInt()

    private fun getDayPositionSelectedDate() =
        ChronoUnit.DAYS.between(MIN_DATE, selectedDate).toInt()
}