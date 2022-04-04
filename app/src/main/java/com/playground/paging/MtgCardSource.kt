package com.playground.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.magicthegathering.kotlinsdk.api.MtgCardApiClient
import io.magicthegathering.kotlinsdk.model.card.MtgCard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MtgCardSource: PagingSource<Int, MtgCard>() {
    private val apiStartingPageIndex = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MtgCard> {
        return withContext(Dispatchers.IO) {
             try {
                val currentPageIndex = params.key ?: apiStartingPageIndex
                val mtgCards = MtgCardApiClient
                    .getAllCards(defaultPageSize, currentPageIndex)
                    .body() ?: emptyList()

                LoadResult.Page(
                    data = mtgCards,
                    prevKey = if (currentPageIndex.isFirstPage()) null else currentPageIndex - 1,
                    nextKey = if (mtgCards.isEmpty()) null else currentPageIndex + 1
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MtgCard>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun Int.isFirstPage() = this == apiStartingPageIndex

    companion object {
        const val defaultPageSize = 15
    }
}