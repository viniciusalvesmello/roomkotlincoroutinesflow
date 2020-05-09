package io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.viniciusalvesmello.roomkotlincoroutinesflow.room.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("select * from product")
    fun selectProducts(): Flow<List<ProductEntity>>

    @Query("select * from product where id = :id")
    fun selectProduct(id: Int): ProductEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntity: ProductEntity): Long?
}