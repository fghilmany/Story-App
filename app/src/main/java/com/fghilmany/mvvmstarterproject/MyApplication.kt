package com.fghilmany.mvvmstarterproject

import android.app.Application
import com.fghilmany.mvvmstarterproject.core.di.databaseModule
import com.fghilmany.mvvmstarterproject.core.di.networkModule
import com.fghilmany.mvvmstarterproject.core.di.repositoryModule
import com.fghilmany.mvvmstarterproject.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule,
                    databaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}