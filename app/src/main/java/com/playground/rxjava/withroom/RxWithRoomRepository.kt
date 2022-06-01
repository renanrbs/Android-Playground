package com.playground.rxjava.withroom

import io.reactivex.rxjava3.core.Observable

class RxWithRoomRepository(private val exampleEntityDao: ExampleEntityDao) {
    fun getEntities(): Observable<List<ExampleEntity>> = exampleEntityDao.getAll()

    fun addEntity(text: String) {
        val entity = ExampleEntity(text = text)
        exampleEntityDao.insertAll(entity)
    }

    fun deleteEntity(entity: ExampleEntity) = exampleEntityDao.delete(entity)

    fun deleteEntityById(id: Int) = exampleEntityDao.deleteById(id)
}