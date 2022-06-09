package com.playground.draganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.draganddrop.DropHelper
import com.playground.ui.theme.PlaygroundTheme

class DragAndDropComposeActivity: AppCompatActivity() {

    private val draggableTexts = listOf(
        "Bolacha Maria",
        "Bolacha de Água e Sal",
        "Biscoito de Polvilho",
        "Waffer",
        "Salgadinho"
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                DragAndDropComposeActivity()
            }
        }
    }

    @Composable
    private fun DragAndDropComposeActivity() {
        Column {
            DropArea()
            
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(draggableTexts) { text ->
                    DraggableText(inputText = text)
                }
            }
        }
    }

    @Composable
    private fun DraggableText(inputText: String) {
        val text by remember { mutableStateOf(inputText) }
        val view = LocalView.current

        Card(
            elevation = 4.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {


                            // Data to be dragged
                            val clipData = ClipData.newPlainText(
                                view.tag as? CharSequence,
                                text
                            )

                            // Item that is dragged
                            val dragShadow = View.DragShadowBuilder(view)

                            view.startDragAndDrop(
                                clipData,         // The data to be dragged
                                dragShadow,       // The drag shadow builder
                                null, // No need to use local data
                                0           // Flags (not currently used, set to 0)
                            )
                        }
                    )
                }
        ) {
            Text(
                text,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    @Composable
    private fun DropArea() {
        var droppedText by remember { mutableStateOf("") }

        Card(
            elevation = 4.dp,
            backgroundColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            DropHelper.configureView(
                this,
                // Target drop view to be highlighted
                LocalView.current,
                // Supported MIME types
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            ) { _, payload ->
                droppedText = payload.clip.getItemAt(0).text.toString()

                payload
            }

            Text(droppedText)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        PlaygroundTheme {
            DragAndDropComposeActivity()
        }
    }
}