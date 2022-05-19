package com.playground.rxjava

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.playground.ui.theme.PlaygroundTheme

class RxJavaActivity : AppCompatActivity() {
    private val viewModel: RxJavaViewModel by viewModels()
    private val data: SnapshotStateList<String> = mutableStateListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Screen()
                }
            }
        }
        viewModel.loadUsersConvert().observe(this, {
            data.clear()
            data.addAll(it)
        })
//        viewModel.loadUsers()
    }

    @Composable
    private fun Screen() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
            ) {
                items(
                    items = data,
                    itemContent = {
                        Text(text = it)
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        PlaygroundTheme {
            Screen()
        }
    }
}

