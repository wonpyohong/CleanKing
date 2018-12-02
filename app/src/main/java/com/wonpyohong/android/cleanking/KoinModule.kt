package com.wonpyohong.android.cleanking

import android.arch.lifecycle.LifecycleOwner
import com.wonpyohong.android.cleanking.ui.add.CategoryAdapter
import com.wonpyohong.android.cleanking.ui.add.StuffAdapter
import com.wonpyohong.android.cleanking.ui.add.WriteStuffHistoryViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val koinModule : Module = module {
    viewModel { WriteStuffHistoryViewModel() }
    factory { (lifeCycleOwner: LifecycleOwner) -> CategoryAdapter(lifeCycleOwner) }
    factory { (lifeCycleOwner: LifecycleOwner) -> StuffAdapter(lifeCycleOwner) }
}