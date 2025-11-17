package com.example.muzzchat.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.muzzchat.R
import com.example.muzzchat.domain.model.ChatMessage
import com.example.muzzchat.domain.model.formatDayOfWeek
import com.example.muzzchat.domain.model.formatTime
import com.example.muzzchat.ui.component.ChatAppBar
import java.time.Instant

@Preview
@Composable
fun PreviewChatScreen() {
    ChatScreen(hiltViewModel(), {})
}

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBackClicked: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val inputText = rememberSaveable { mutableStateOf("") }
    var currentUser by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            ChatAppBar(
                isMe = currentUser,
                onBackClicked = { onBackClicked() },
                userToggled = { user ->
                    currentUser = user
                })
        }
    ) { contentPadding ->
        Column(modifier = Modifier
            .padding(contentPadding)
            .background(Color.White)
            .fillMaxSize()
            .consumeWindowInsets(contentPadding)
            .imePadding()) {

            MessageListView(Modifier
                .fillMaxSize()
                .weight(1f),
                 messageList = uiState.messageList)

            InputTextFooterView(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
                value = inputText.value,
                onValueChange = {
                    inputText.value = it
                },
                onSendClicked = {
                    if (inputText.value.isNotEmpty()) {
                        viewModel.saveMessage(inputText.value, !currentUser)
                        inputText.value = ""
                    }
                })

        }
    }
}

@Preview
@Composable
fun PreviewMessageListView() {
    MessageListView(Modifier
        .fillMaxSize()
        .background(Color.White),
        messageList = mutableListOf<ChatMessage>().apply {
            add(ChatMessage("This is line 1", false, Instant.now(), true, false))
            add(ChatMessage("This is line 2\nThis is also another line in line 2 with a much longer text that overflows the lines",false, Instant.now(),false, true))
            add(ChatMessage("This is line 1", true, Instant.now(), true, false))
            add(ChatMessage("This is line 2", true, Instant.now(), false, true))
            add(ChatMessage("This is line 1", false, Instant.now(), false, false))
        })
}

@Composable
fun MessageListView(modifier: Modifier,
                    messageList: List<ChatMessage>
                    ) {

    Box(modifier = modifier
        .fillMaxWidth()
        .bottomOuterShadow()) {

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .testTag("message_list"),
            reverseLayout = true,
        ) {
            itemsIndexed(messageList.reversed()) { index, item ->

                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (item.isSectionRequired) {
                        Row(
                            modifier = Modifier.padding(top = 12.dp),
                        ) {
                            Text(
                                modifier = Modifier,
                                text = item.timestamp.formatDayOfWeek(),
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colorResource(R.color.message_section_text)
                                ),
                            )

                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = item.timestamp.formatTime(),
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    color = colorResource(R.color.message_section_text)
                                ),
                            )
                        }
                    }

                    val background = if (item.isRecipient)
                        colorResource(R.color.message_me_background) else
                        colorResource(R.color.message_recipient_background)
                    val textColour = if (item.isRecipient) colorResource(R.color.default_text) else Color.White
                    val shape = if (item.isRecipient)
                        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp) else
                        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
                    val alignment = if (item.isRecipient) Alignment.CenterStart else Alignment.CenterEnd

                    Box(modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = alignment
                    ) {
                        Box(modifier = Modifier
                            .padding(start = if (item.isRecipient) 12.dp else 80.dp)
                            .padding(end = if (item.isRecipient) 80.dp else 12.dp)
                            .padding(top = if (item.isClusterMessage) 6.dp else 12.dp)
                            .padding(bottom = if (index == 0) 12.dp else 0.dp)
                            .background(color = background, shape = shape),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 16.dp),
                                text = item.message,
                                style = TextStyle(
                                    color = textColour,
                                    fontSize = 18.sp
                                ),
                            )
                        }
                    }
                }
            }
        }

    }


}

@Preview
@Composable
fun PreviewInputTextFooterView() {
    InputTextFooterView(modifier = Modifier
        .background(Color.White)
        .fillMaxWidth(),
        value = "",
        onValueChange = {},
        onSendClicked = {})
}

@Composable
fun InputTextFooterView(modifier: Modifier,
                        value: String,
                        onValueChange: (String) -> Unit,
                        onSendClicked: () -> Unit
                        ) {

    Box(modifier = modifier
        .fillMaxWidth()) {

        Row(modifier = Modifier
            .padding(12.dp)
            .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically) {

            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
                    .testTag("chat_input"),
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.pink),
                    unfocusedBorderColor = colorResource(R.color.pink),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                ),
                value = value,
                onValueChange = onValueChange
            )

            IconButton(
                modifier = Modifier.size(54.dp)
                    .testTag("send_button"),
                onClick = onSendClicked,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = colorResource(R.color.pink),
                    contentColor = colorResource(R.color.white),
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = colorResource(R.color.white)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

fun Modifier.bottomOuterShadow(
    shadowHeight: Dp = 5.dp,
    color: Color = Color.Black.copy(alpha = 0.1f)
) = drawWithContent {
    drawContent()

    val heightPx = shadowHeight.toPx()

    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, color),  // fade UPWARD
            startY = size.height - heightPx,
            endY = size.height
        ),
        topLeft = Offset(0f, size.height - heightPx),
        size = Size(size.width, heightPx)
    )
}

