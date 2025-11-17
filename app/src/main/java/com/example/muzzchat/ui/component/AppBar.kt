package com.example.muzzchat.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.muzzchat.R

@Preview
@Composable
fun PreviewChatAppBar(){
    ChatAppBar(true, {},{})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatAppBar(
    isMe: Boolean,
    onBackClicked: () -> Unit,
    userToggled: (Boolean) -> Unit
){
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        windowInsets = WindowInsets(0,0,0,0),
        modifier = Modifier.shadow(elevation = 4.dp),
        title = {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(R.drawable.profile_pic),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Fit
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "Sarah",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
        },
        actions = {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    modifier = Modifier.rotate(90f),
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "More"
                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                val user = if (isMe) "Me" else "Sarah"

                DropdownMenuItem(
                    text = { Text("Toggle user ($user)") },
                    onClick = {
                        showMenu = false
                        userToggled(!isMe)
                    }
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                onBackClicked()
            }) {
                Icon(
                    Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    "backIcon",
                    tint = colorResource(R.color.pink),
                    modifier = Modifier.fillMaxSize(),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}