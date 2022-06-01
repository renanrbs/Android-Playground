package com.playground.rxjava.withroom

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.playground.ui.theme.PlaygroundTheme
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RxWithRoomActivity : AppCompatActivity() {
    private lateinit var database: ExampleDatabase
    private lateinit var repository: RxWithRoomRepository
    private lateinit var viewModel: RxWithRoomViewModel

    private val retrievedEntities = mutableStateListOf<ExampleEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = ExampleDatabase.getDatabase(applicationContext)
        repository = RxWithRoomRepository(database.exampleEntityDao())
        viewModel = viewModels<RxWithRoomViewModel> {
            RxWithRoomViewModelFactory(repository)
        }.value

        viewModel.getAllEntities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { entities ->
                retrievedEntities.clear()
                retrievedEntities.addAll(entities)
            }

        setContent {
            PlaygroundTheme {
                ScreenContent()
            }
        }
    }

    @Composable
    private fun ScreenContent() {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(Modifier.weight(0.9f)) {
                EntitiesCardList(entities = retrievedEntities)
            }
            
            Button(onClick = { viewModel.addRandomEntity() }) {
                Text("Add entity")
            }
        }
    }

    @Composable
    fun EntitiesCardList(entities: List<ExampleEntity>) {
        LazyColumn(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(entities) { EntityCard(it) }
        }
    }

    @Composable
    fun EntityCard(entity: ExampleEntity) {
        Card(
            Modifier.fillMaxWidth()
                .clickable { viewModel.deleteEntity(entity) }
        ) {
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = "id: ${entity.id}",
                    fontStyle = FontStyle.Italic,
                    fontSize = 10.sp
                )
                Text(
                    text = entity.text,
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        PlaygroundTheme {
            ScreenContent()
        }
    }
}