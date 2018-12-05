package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.support.v4.view.ViewCompat.startDragAndDrop
import android.util.Log
import android.view.View
import com.wonpyohong.android.cleanking.base.BaseViewModel
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.room.stuff.StuffDatabase
import com.wonpyohong.android.cleanking.room.stuff.StuffHistory
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime

class WriteStuffHistoryViewModel: BaseViewModel() {
    var selectedCategory = MutableLiveData<Category>()
    var selectedStuff = MutableLiveData<Stuff>()

    lateinit var categoryList: LiveData<List<Category>>

    lateinit var stuffList: LiveData<MutableList<Stuff>>

    var newStuffName = MutableLiveData<String>()

    private val actionSignal: PublishProcessor<ACTION> = PublishProcessor.create()

    private val mergeStuffSignal: PublishProcessor<MergeStuff> = PublishProcessor.create()

    data class MergeStuff(val child: Stuff, val parent: Stuff)

    fun getActionSignal(): LiveData<ACTION> {
        return LiveDataReactiveStreams.fromPublisher(actionSignal)
    }

    fun getMergeStuffSignal(): LiveData<MergeStuff> {
        return LiveDataReactiveStreams.fromPublisher(mergeStuffSignal)
    }

    fun observeCategoryList() {
        categoryList = StuffDatabase.getInstance().getCategoryDao().getAllCategoryList()
    }

    fun observeStuffListOnSelectedCategory() {
        stuffList = Transformations.switchMap(selectedCategory) { selectedCategory ->
            StuffDatabase.getInstance().getStuffDao().getStuffList(selectedCategory.categoryId)
        }
    }

    fun onCategoryClicked(category: Category) {
        categoryList.value!!.forEach {
            it.setSelectedFalse()
        }
        category.onClicked()
        selectedCategory.value = category
    }

    fun onStuffClicked(stuff: Stuff) {
        stuffList.value!!.forEach {
            it.setSelectedFalse()
        }
        stuff.onClicked()
        selectedStuff.value = stuff
    }

    fun onStuffLongClicked(view: View, stuff: Stuff): Boolean {
        view.startDragAndDrop(null, View.DragShadowBuilder(view), Pair(view, stuff), 0)
        view.alpha = 0.5f

        return true
    }

    fun requestMergeStuff(child: Stuff, parent: Stuff) {
        mergeStuffSignal.offer(MergeStuff(child, parent))
    }

    fun onMergeStuffConfirmed(mergeStuff: MergeStuff) {
        mergeStuff.parent.frequency += mergeStuff.child.frequency
        val stuffDatabase = StuffDatabase.getInstance()
        addDisposable(Single.fromCallable {
                stuffDatabase.getStuffDao().update(mergeStuff.parent)
                stuffDatabase.getStuffDao().changeStuffId(mergeStuff.child.id, mergeStuff.parent.id)
                stuffDatabase.getStuffDao().delete(mergeStuff.child)

                stuffDatabase.setTransactionSuccessful()
                stuffDatabase.endTransaction()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // success
            },
            {
                it.printStackTrace()
                if (stuffDatabase.inTransaction()) {
                    stuffDatabase.endTransaction()
                }
            })
        )
    }

    fun addStuff() {
        selectedCategory.value?.let {
            if (!newStuffName.value.isNullOrEmpty()) {
                val newStuff = Stuff(
                    0,
                    it.categoryId,
                    newStuffName.value!!,
                    0
                )

                Completable.fromAction {
                    StuffDatabase.getInstance().getStuffDao().insert(newStuff)
                }.subscribeOn(Schedulers.io())
                    .subscribe()
            }
        } ?:
            actionSignal.offer(ACTION.CATEGORY_NOT_SELECTED)
    }

    fun writeHistory() {
        if (selectedCategory.value == null) {
            actionSignal.offer(ACTION.CATEGORY_NOT_SELECTED)
            return
        }

        if (selectedStuff.value == null) {
            actionSignal.offer(ACTION.STUFF_NOT_SELECTED)
            return
        }

        val stuffHistory = StuffHistory(0, LocalDateTime.now().minusHours(4).toLocalDate().toString(),
            selectedStuff.value!!.id)

        val stuffDatabase = StuffDatabase.getInstance()
        addDisposable(Single.fromCallable {
            val stuff = stuffList.value?.find { it.categoryId == selectedCategory.value!!.categoryId && it.id == stuffHistory.stuffId}
            stuffDatabase.beginTransaction()
            if (stuff != null) {
                stuff.frequency++

                stuffDatabase.getStuffDao().update(stuff)
                stuffDatabase.getStuffHistoryDao().insert(stuffHistory)
                stuffDatabase.setTransactionSuccessful()
                stuffDatabase.endTransaction()
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            RxDayDataSetChangedEvent.sendEvent(RxDayDataSetChangedEvent.DayDataSetChanged())

            actionSignal.offer(ACTION.FINISH)
        }, {
            it.printStackTrace()
            if (stuffDatabase.inTransaction()) {
                stuffDatabase.endTransaction()
            }
        })
        )
    }

    enum class ACTION {
        CATEGORY_NOT_SELECTED,
        STUFF_NOT_SELECTED,
        FINISH,
    }
}