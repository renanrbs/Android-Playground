package com.playground

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.text.font.FontStyle

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