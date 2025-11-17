package com.example.muzzchat.ui.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.muzzchat.R

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen({})
}

@Composable
fun MainScreen(
    onNavigateToChat: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            modifier = Modifier,
            painter = painterResource(R.drawable.muzz_logo),
            contentDescription = "Profile picture",
            contentScale = ContentScale.Fit
        )

        Button(
            onClick = onNavigateToChat
        ) {
            Text("Enter Chat")
        }
    }
}