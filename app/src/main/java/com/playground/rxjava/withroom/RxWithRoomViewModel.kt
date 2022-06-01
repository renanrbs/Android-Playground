package com.playground.rxjava.withroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RxWithRoomViewModel(private val repository: RxWithRoomRepository) : ViewModel() {
    private val randomWordList = listOf(
        "shoes", "yellow", "alike", "join", "subsequent", "inform", "dislike", "horse", "dizzy", "keen",
        "harm", "literate", "diligent", "freezing", "hobbies", "one", "pale", "second-hand", "brash", "wanting",
        "concern", "plausible", "needle", "continue", "wriggle", "tip", "mindless", "strap", "bumpy", "carry",
        "page", "ceaseless", "arithmetic", "abaft", "pop", "kindly", "sock", "stiff", "ambiguous", "many",
        "bite", "overconfident", "trot", "boiling", "tax", "realize", "jumbled", "future", "dinosaurs", "aboard"
    )

    fun getAllEntities(): Observable<List<ExampleEntity>> {
        return repository.getEntities()
    }

    fun addRandomEntity() {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.addEntity(randomWordList.random())
        }
    }

    fun deleteEntity(entity: ExampleEntity) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.deleteEntity(entity)
        }
    }

    fun deleteEntityById(id: Int) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.deleteEntityById(id)
        }
    }
}

class RxWithRoomViewModelFactory(private val repository: RxWithRoomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RxWithRoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RxWithRoomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}