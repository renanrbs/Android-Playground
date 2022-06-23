package com.playground.navigationapp.screens

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.playground.navigationapp.navigation.AppScreens
import com.playground.navigationapp.ui.theme.PlaygroundTheme

@Composable
fun HomeScreen(navigationController: NavController) {
    var textInput by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        TextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Write something, please") },
            modifier = Modifier.fillMaxWidth(0.85f)
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                navigationController.navigate("${AppScreens.DetailScreen.route}?textInput=$textInput")
            },
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text("Go!")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    PlaygroundTheme {
        HomeScreen(rememberNavController())
    }
}