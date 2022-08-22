package com.playground.draganddrop

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.draganddrop.DropHelper
import com.microsoft.device.dualscreen.draganddrop.*
import com.playground.ui.theme.PlaygroundTheme
import com.playground.ui.theme.Purple200
import com.playground.ui.theme.Purple500

class MicrosoftDragAndDropComposeActivity: AppCompatActivity() {

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
                MicrosoftDragAndDropComposeActivity()
            }
        }
    }

    @SuppressLint("NotConstructor")
    @Composable
    private fun MicrosoftDragAndDropComposeActivity() {
        DragContainer {
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
    }

    @Composable
    private fun DraggableText(inputText: String) {
        val dragData = DragData(type = MimeType.TEXT_PLAIN, data = inputText)

        DragTarget(dragData = dragData) {
            Card(
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    inputText,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }

    @Composable
    private fun DropArea() {
        var droppedText by remember { mutableStateOf("-") }
        var isDroppingItem by remember { mutableStateOf(false) }
        var isItemInBounds by remember { mutableStateOf(false) }

        DropContainer(
            modifier = Modifier,
            onDrag = { inBounds, isDragging ->
                isDroppingItem = isDragging
                isItemInBounds = inBounds
            }
        ) { dragData ->
            val cardColor = if (isDroppingItem && isItemInBounds) {
                Purple500
            } else if (isDroppingItem) {
                Purple200
            } else {
                MaterialTheme.colors.surface
            }

            dragData?.let {
                if(!isDroppingItem) {
                    droppedText = it.data as String
                }
            }

            Card(
                elevation = 4.dp,
                backgroundColor = cardColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
            ) {
                Text(
                    "Drag your text to card:",
                    textAlign = TextAlign.Center
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        droppedText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                }
            }
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