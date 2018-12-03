package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
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

    lateinit var stuffList: LiveData<List<Stuff>>

    var newStuffName = MutableLiveData<String>()

    private val actionSignal: PublishProcessor<ACTION> = PublishProcessor.create()

    fun getActionSignal(): LiveData<ACTION> {
        return LiveDataReactiveStreams.fromPublisher(actionSignal)
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

    fun writeHistory(): Boolean {
        if (selectedCategory.value == null) {
            actionSignal.offer(ACTION.CATEGORY_NOT_SELECTED)

            return true
        }

        if (selectedStuff.value == null) {
            actionSignal.offer(ACTION.STUFF_NOT_SELECTED)

            return true
        }

        val stuffHistory = StuffHistory(0, LocalDateTime.now().minusHours(4).toLocalDate().toString(),
            selectedCategory.value!!.categoryName,
            selectedStuff.value!!.stuffName)

        addDisposable(Single.fromCallable {
            val stuff = stuffList.value?.find { it.categoryId == selectedCategory.value!!.categoryId && it.stuffName == stuffHistory.stuffName}
            StuffDatabase.getInstance().beginTransaction()
            if (stuff != null) {
                stuff.frequency++

                StuffDatabase.getInstance().getStuffDao().update(stuff)
                StuffDatabase.getInstance().getStuffHistoryDao().insert(stuffHistory)
                StuffDatabase.getInstance().setTransactionSuccessful()
            }
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            RxDayDataSetChangedEvent.sendEvent(RxDayDataSetChangedEvent.DayDataSetChanged())

            StuffDatabase.getInstance().endTransaction()
            actionSignal.offer(ACTION.FINISH)
        }, {
            it.printStackTrace()
            StuffDatabase.getInstance().endTransaction()
        })
        )

        return true
    }

    private fun <T: android.databinding.Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
        addOnPropertyChangedCallback(
            object: android.databinding.Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(
                    observable: android.databinding.Observable?, i: Int) =
                    callback(observable as T)
            })

    enum class ACTION {
        CATEGORY_NOT_SELECTED,
        STUFF_NOT_SELECTED,
        FINISH,
    }
}