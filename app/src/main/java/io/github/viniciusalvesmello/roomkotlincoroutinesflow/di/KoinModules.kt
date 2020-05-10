package io.github.viniciusalvesmello.roomkotlincoroutinesflow.di

import androidx.room.Room
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductRepositoryImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductsRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.GetProductsRepositoryImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.SaveProductRepository
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.repository.SaveProductRepositoryImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.AppDatabase
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.AppDatabase.Companion.DATABASE_NAME
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.AppDatabase.Companion.START_DATABASE
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutines
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppCoroutinesImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppDispatchers
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.utils.AppDispatchersImpl
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.viewmodel.ProductsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val coroutinesModule = module {
    single<AppCoroutines> { AppCoroutinesImpl() }
    single<AppDispatchers> { AppDispatchersImpl() }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, DATABASE_NAME)
            .createFromAsset(START_DATABASE)
            .build()
    }
    single { get<AppDatabase>().productDao() }
}

val productsModule = module {
    viewModel { ProductsViewModel(get(), get(), get(), get()) }
    single<GetProductsRepository> { GetProductsRepositoryImpl(get(), get()) }
    single<GetProductRepository> { GetProductRepositoryImpl(get()) }
    single<SaveProductRepository> { SaveProductRepositoryImpl(get()) }
}

