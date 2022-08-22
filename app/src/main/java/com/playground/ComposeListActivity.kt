package com.playground

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.playground.animation.AnimationActivity
import com.playground.biometric.BiometricActivity
import com.playground.camera.CameraActivity
import com.playground.datastore.DataStoreActivity
import com.playground.draganddrop.DragAndDropActivity
import com.playground.draganddrop.DragAndDropComposeActivity
import com.playground.draganddrop.MicrosoftDragAndDropComposeActivity
import com.playground.paging.PagingActivity
import com.playground.rxjava.RxJavaActivity
import com.playground.rxjava.withroom.RxWithRoomActivity
import com.playground.security.SecurityActivity
import com.playground.ui.theme.PlaygroundTheme

class ComposeListActivity : ComponentActivity() {
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
    }
}

@Composable
private fun Screen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Android Playground",
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActivitiesList()
    }
}

@Composable
private fun ActivitiesList() {
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = listOf(
                ActivityInfo("Animation", AnimationActivity::class.java),
                ActivityInfo("Data Store", DataStoreActivity::class.java),
                ActivityInfo("Camera", CameraActivity::class.java),
                ActivityInfo("Biometry", BiometricActivity::class.java),
                ActivityInfo("Security", SecurityActivity::class.java),
                ActivityInfo("Paging", PagingActivity::class.java),
                ActivityInfo("Drag and Drop", DragAndDropActivity::class.java),
                ActivityInfo("Drag and Drop with Compose", DragAndDropComposeActivity::class.java),
                ActivityInfo("Microsoft's Drag and Drop with Compose", MicrosoftDragAndDropComposeActivity::class.java),
                ActivityInfo("RX Java", RxJavaActivity::class.java),
                ActivityInfo("RX Java with Room", RxWithRoomActivity::class.java)
            ),
            itemContent = {
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { openActivity(context, it.activity) }
                ) {
                    Text(text = it.label, textAlign = TextAlign.Center)
                }
            }
        )
    }
}

data class ActivityInfo(val label: String, val activity: Class<*>)

private fun openActivity(context: Context, targetActivity: Class<*>) {
    val intent = Intent(context, targetActivity)
    context.startActivity(intent)
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    PlaygroundTheme {
        Screen()
    }
}