package io.github.viniciusalvesmello.roomkotlincoroutinesflow.room

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao.ProductDao
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class
    ],
    version = 1
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        val DATABASE_NAME = "products.db"
        val START_DATABASE = "database/start_products.db"
    }
}