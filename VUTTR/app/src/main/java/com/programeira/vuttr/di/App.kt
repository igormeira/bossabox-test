package com.programeira.vuttr.di

import android.app.Application
import com.programeira.vuttr.data.datasource.ApiService
import com.programeira.vuttr.data.datasource.ConnectivityService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single { ConnectivityService() }
    single { ApiService.getBaseService() }
}

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}