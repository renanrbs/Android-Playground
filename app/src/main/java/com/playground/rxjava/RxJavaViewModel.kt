package com.playground.rxjava

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


class RxJavaViewModel : ViewModel() {

    private var disposable: Disposable? = null

    private val data: MutableLiveData<List<String>> by lazy {
        MutableLiveData<List<String>>()
    }

    fun getData(): LiveData<List<String>> {
        return data
    }

    fun loadUsers() {
        disposable = RxJavaRepository.getData()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                data.postValue(it)
            }
    }

    fun loadUsersConvert(): LiveData<List<String>> {
        return LiveDataReactiveStreams.fromPublisher(
            RxJavaRepository.getData().toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.newThread())
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}