package com.example.desafio2.di

import com.example.desafio2.data.Repository
import com.example.desafio2.ui.NewUserFragmentViewModel
import com.example.desafio2.ui.UpdateUserFragmentViewModel
import com.example.desafio2.ui.UserListFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    single {
        Repository(androidContext())
    }

    viewModel {
        NewUserFragmentViewModel(get())
    }
    viewModel {
        UpdateUserFragmentViewModel(get())
    }
    viewModel {
        UserListFragmentViewModel(get())
    }
}