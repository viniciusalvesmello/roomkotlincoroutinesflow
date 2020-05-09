package io.github.viniciusalvesmello.roomkotlincoroutinesflow

import android.app.Application
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.di.coroutinesModule
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.di.databaseModule
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.di.productsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoinModules()
    }

    private fun startKoinModules() {
        startKoin {
            androidContext(this@AppApplication)
            androidLogger()
            modules(coroutinesModule, databaseModule, productsModule)
        }
    }
}