package com.wonpyohong.android.cleanking.ui.add

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.room.stuff.Stuff
import com.wonpyohong.android.cleanking.room.stuff.StuffDatabase
import com.wonpyohong.android.cleanking.room.stuff.StuffHistory
import com.wonpyohong.android.cleanking.support.RxDayDataSetChangedEvent
import com.wonpyohong.android.cleanking.support.recyclerview.DragHelperCallback
import com.wonpyohong.android.cleanking.support.recyclerview.ItemTouchHelperAdapter
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime

class WriteStuffHistoryViewModel {
    var selectedCategory = ObservableField<Category>()
    var selectedStuff = ObservableField<Stuff>()

    var categoryList = ObservableArrayList<Category>()
    var stuffList = ObservableArrayList<Stuff>()

    var newStuffName = ObservableField<String>()

    private val actionSignal: PublishProcessor<ACTION> = PublishProcessor.create()

    fun getActionSignal(): LiveData<ACTION> {
        return LiveDataReactiveStreams.fromPublisher(actionSignal)
    }

    fun initCategoryRecyclerView(categoryRecyclerView: RecyclerView, chipsLayoutManager: ChipsLayoutManager) {
        val categoryAdapter = CategoryAdapter(viewModel)
        initRecyclerViewCommon(categoryRecyclerView, categoryAdapter, chipsLayoutManager)

        StuffDatabase.getInstance().getCategoryDao().getAllCategoryList().subscribe {
            viewModel.categoryList.clear()
            viewModel.categoryList.addAll(it)
        }
    }

    fun initStuffRecyclerView(stuffRecyclerView: RecyclerView, chipsLayoutManager: ChipsLayoutManager) {
        val stuffAdapter = StuffAdapter(viewModel)
        initRecyclerViewCommon(stuffRecyclerView, stuffAdapter, chipsLayoutManager)

        viewModel.selectedCategory.addOnPropertyChanged {selectedObservableCategory ->
            StuffDatabase.getInstance().getStuffDao().getStuffList(selectedObservableCategory.get()!!.categoryId)
                .subscribe {list ->
                    viewModel.stuffList.clear()
                    viewModel.stuffList.addAll(list)
                } }

    }

    private fun initRecyclerViewCommon(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, chipsLayoutManager: ChipsLayoutManager) {
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = chipsLayoutManager
        recyclerView.addItemDecoration(
            SpacingItemDecoration(20, 20)
        )

        val touchHelper = ItemTouchHelper(DragHelperCallback(adapter as ItemTouchHelperAdapter))
        touchHelper.attachToRecyclerView(recyclerView)
    }

    fun onCategoryClicked(category: Category) {
        categoryList.forEach {
            it.setSelectedFalse()
        }
        category.onClicked()
        selectedCategory.set(category)
    }

    fun onStuffClicked(stuff: Stuff) {
        stuffList.forEach {
            it.setSelectedFalse()
        }
        stuff.onClicked()
        selectedStuff.set(stuff)
    }

    fun addStuff() {
        viewModel.selectedCategory.get()?.let {
            if (!newStuffName.get().isNullOrEmpty()) {
                val newStuff = Stuff(
                    0,
                    it.categoryId,
                    newStuffName.get()!!
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
        if (viewModel.selectedCategory.get() == null) {
            actionSignal.offer(ACTION.CATEGORY_NOT_SELECTED)

            return true
        }

        if (viewModel.selectedStuff.get() == null) {
            actionSignal.offer(ACTION.STUFF_NOT_SELECTED)

            return true
        }

        val stuff = StuffHistory(0, LocalDateTime.now().minusHours(4).toLocalDate().toString(),
            viewModel.selectedCategory.get()!!.categoryName,
            viewModel.selectedStuff.get()!!.stuffName)

        Single.just(1)
            .subscribeOn(Schedulers.io())
            .map {
                StuffDatabase.getInstance().getStuffHistoryDao().insert(stuff)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ exerciseId ->
                RxDayDataSetChangedEvent.sendEvent(RxDayDataSetChangedEvent.DayDataSetChanged())

                actionSignal.offer(ACTION.FINISH)
            }, {
                it.printStackTrace()
            })

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