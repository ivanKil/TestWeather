package com.lessons.weatherappandroid


import com.lessons.nasa.network.RetrofitServices
import com.lessons.weatherappandroid.ui.main.MainFragmentJava
import com.lessons.weatherappandroid.ui.main.MainViewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val baseModule: Module = module {

    viewModel { MainViewModel(get()) }

    single { RetrofitServices.create() }

    scope<MainActivity> { fragment { MainFragmentJava() } }
}
