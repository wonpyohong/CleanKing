package com.wonpyohong.android.cleanking.ui.add

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import com.wonpyohong.android.cleanking.room.stuff.Category
import com.wonpyohong.android.cleanking.room.stuff.Stuff

class WriteStuffHistoryViewModel {
    var selectedCategory = ObservableField<Category>()
    var selectedStuff = ObservableField<Stuff>()

    var categoryList = ObservableArrayList<Category>()
    var stuffList = ObservableArrayList<Stuff>()

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
}