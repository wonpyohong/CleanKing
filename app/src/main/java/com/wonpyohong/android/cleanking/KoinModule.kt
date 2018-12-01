package com.wonpyohong.android.cleanking

import com.wonpyohong.android.cleanking.ui.add.WriteStuffHistoryViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val koinModule : Module = module {
    viewModel { WriteStuffHistoryViewModel() }
}