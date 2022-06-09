package com.playground.rxjava.withroom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExampleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String
)