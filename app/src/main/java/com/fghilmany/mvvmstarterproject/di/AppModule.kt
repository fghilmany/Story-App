package com.fghilmany.mvvmstarterproject.di

import com.fghilmany.mvvmstarterproject.ui.add.AddStoryViewModel
import com.fghilmany.mvvmstarterproject.ui.home.HomeViewModel
import com.fghilmany.mvvmstarterproject.ui.login.LoginViewModel
import com.fghilmany.mvvmstarterproject.ui.maps.MapsViewModel
import com.fghilmany.mvvmstarterproject.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AddStoryViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}