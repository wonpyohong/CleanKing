package com.wonpyohong.android.cleanking.ui

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.homedev.android.dietapp.room.exercise.Dump
import com.homedev.android.dietapp.room.exercise.DumpDatabase
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_add_dump.*
import org.threeten.bp.LocalDate

class AddDumpDialog: DialogFragment() {
    lateinit var customView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return customView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        customView = activity!!.layoutInflater.inflate(R.layout.dialog_add_dump, null)
        builder.setView(customView)
            .setTitle("정리할 물건")
            .setPositiveButton(R.string.add) { dialog, id ->
                val dump = Dump(0, LocalDate.now().toString(), category.text.toString(), dumpName.text.toString(), reason.text.toString())

                Single.just(1)
                    .subscribeOn(Schedulers.io())
                    .map {
                        DumpDatabase.getInstance().getDumpDao().insert(dump)
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({exerciseId ->
                        RxDayDataSetChangedEvent.sendEvent(RxDayDataSetChangedEvent.DayDataSetChanged())
                        dialog.dismiss()
                    }, {
                        it.printStackTrace()
                    })
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
        return builder.create()
    }
}