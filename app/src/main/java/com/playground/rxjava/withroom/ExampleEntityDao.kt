package com.playground.rxjava.withroom

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Observable

@Dao
interface ExampleEntityDao {
    @Query("SELECT * FROM ExampleEntity")
    fun getAll(): Observable<List<ExampleEntity>>

    @Insert
    fun insertAll(vararg entities: ExampleEntity)

    @Delete
    fun delete(entity: ExampleEntity)

    @Query("DELETE FROM ExampleEntity WHERE id = :id")
    fun deleteById(id: Int)
}