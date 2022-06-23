package com.playground.navigationapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.playground.navigationapp.ui.theme.PlaygroundTheme

@Composable
fun DetailScreen(navigationController: NavController, text: String?) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text("Text inputted on previous screen:",
            modifier = Modifier.fillMaxWidth(0.85f),
            textAlign = TextAlign.Center
        )

        Text(text = "\"$text\"",
            modifier = Modifier.fillMaxWidth(0.85f),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    PlaygroundTheme {
        DetailScreen(rememberNavController(), "Test text")
    }
}