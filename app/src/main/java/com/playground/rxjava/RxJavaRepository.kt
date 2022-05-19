package com.playground.rxjava

import io.reactivex.rxjava3.core.Observable

object RxJavaRepository {

    fun getData(): Observable<List<String>> {
        return Observable.just(listOf("one", "two", "three", "four", "five"))
    }
}