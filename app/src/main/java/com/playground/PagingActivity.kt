package com.playground

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.playground.paging.MtgCardSource
import com.playground.ui.theme.PlaygroundTheme
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.coroutines.flow.Flow

class PagingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                PagingActivityContent()
            }
        }
    }

    @Composable
    fun PagingActivityContent() {
        val mtgCards = Pager(PagingConfig(MtgCardSource.defaultPageSize)) {
            MtgCardSource()
        }.flow

        MtgCardList(cards = mtgCards)
    }

    @Composable
    fun MtgCardList(cards: Flow<PagingData<MtgCard>>) {
        val lazyCardList: LazyPagingItems<MtgCard> = cards.collectAsLazyPagingItems()

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lazyCardList) { card ->
                MtgCardItem(cardInfo = card!!)
            }

            val refreshState = lazyCardList.loadState.refresh
            if (refreshState is LoadState.Loading)
                item {
                    Column(modifier = Modifier.fillParentMaxSize()) {
                        Text("Initial data fetch ...")
                        CircularProgressIndicator()
                    }
                }
            else if (refreshState is LoadState.Error)
                item {
                    Column(modifier = Modifier.fillParentMaxSize()) {
                        val error = refreshState.error

                        Text("Initial Data Error: ${error.localizedMessage}")
                        Button(onClick = { lazyCardList.retry() }) {
                            Text("Retry")
                        }
                    }
                }

            val appendState = lazyCardList.loadState.append
            if (appendState is LoadState.Loading)
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Subsequent data fetch ...")
                        CircularProgressIndicator()
                    }
                }
            else if (appendState is LoadState.Error)
                item {
                    val error = appendState.error
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Subsequent data Error: ${error.localizedMessage}")
                        Button(onClick = { lazyCardList.retry() }) {
                            Text("Retry")
                        }
                    }
                }
        }
    }

    @Composable
    fun MtgCardItem(cardInfo: MtgCard) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(8.dp)) {
                Text(
                    text = cardInfo.name,
                )
                Text(
                    text = cardInfo.type,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PlaygroundTheme {
            PagingActivityContent()
        }
    }
}