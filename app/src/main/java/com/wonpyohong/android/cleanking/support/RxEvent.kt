package com.wonpyohong.android.cleanking.support

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject



/**
 * Created by wonpyohong on 2017. 10. 15..
 */
object RxDayDataSetChangedEvent {
    private val subject = PublishSubject.create<DayDataSetChanged>()

    val events: Observable<DayDataSetChanged>
        get() = subject

    fun sendEvent(event: DayDataSetChanged) {
        subject.onNext(event)
    }

    class DayDataSetChanged
}

object RxAnyEvent {
    private val subject = PublishSubject.create<Any>()

    val events: Observable<Any>
        get() = subject

    fun sendEvent(event: Any) {
        subject.onNext(event)
    }
}