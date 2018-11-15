package com.wonpyohong.android.cleanking.ui.add

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.*
import com.cunoraz.tagview.Tag
import com.homedev.android.dietapp.room.exercise.Dump
import com.homedev.android.dietapp.room.exercise.DumpDatabase
import com.wonpyohong.android.cleanking.R
import com.wonpyohong.android.cleanking.base.BaseFragment
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import com.wonpyohong.android.cleanking.type.CategoryEnum
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_dump.*
import org.threeten.bp.LocalDate

class AddDumpFragment: BaseFragment() {
    override var fragmentLayoutId = R.layout.fragment_add_dump

    var selectedCategory: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initCategoryTag()
    }

    private fun initCategoryTag() {
        val tagList = CategoryEnum.values().map {
            val tag = Tag(it.title)
            tag.layoutColor = it.color
            tag
        }

        categoryTagView.addTags(tagList)
        categoryTagView.setOnTagClickListener { tag, position ->
            selectedCategory = tag.text
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_dump, menu)
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            return addDump()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun addDump(): Boolean {
        if (selectedCategory == null) {
            AlertDialog.Builder(context!!)
                .setMessage("카테고리를 선택하셔야 합니다")
                .show()

            return true
        }

        val dump = Dump(0, LocalDate.now().toString(), selectedCategory!!, dumpName.text.toString(), reason.text.toString())

        Single.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                DumpDatabase.getInstance().getDumpDao().insert(dump)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ exerciseId ->
                RxDayDataSetChangedEvent.sendEvent(RxDayDataSetChangedEvent.DayDataSetChanged())

                activity?.finish()
            }, {
                it.printStackTrace()
            })

        return true
    }
}