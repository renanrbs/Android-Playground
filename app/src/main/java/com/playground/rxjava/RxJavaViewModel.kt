package com.playground.rxjava

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RxJavaViewModel : ViewModel() {

    private val data: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    fun getData(): LiveData<List<String>> {
        return data
    }

    fun loadUsers() {
        data.postValue(listOf("test"))
    }
}