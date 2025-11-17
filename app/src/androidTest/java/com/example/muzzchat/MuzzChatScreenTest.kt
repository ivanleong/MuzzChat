package com.example.muzzchat


import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.muzzchat.domain.model.ChatMessage
import com.example.muzzchat.ui.presentation.ChatScreen
import com.example.muzzchat.ui.presentation.ChatUiState
import com.example.muzzchat.ui.presentation.ChatViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class MuzzChatScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun messagesAreDisplayedFromMessageListInViewModel() {
        val fakeMessages = listOf(
            ChatMessage("Hello", false, Instant.now(), true, false),
            ChatMessage("How are you?", true, Instant.now(), false, false),
        )

        val mockViewModel = mockk<ChatViewModel>()
        every { mockViewModel.uiState } returns
                MutableStateFlow(ChatUiState(messageList = fakeMessages))

        composeTestRule.setContent {
            ChatScreen(
                viewModel = mockViewModel,
                onBackClicked = {}
            )
        }

        composeTestRule.onNodeWithText("Hello").assertExists()
        composeTestRule.onNodeWithText("How are you?").assertExists()
    }

    @Test
    fun messageTypedInInputIsShownInInputTextAndMessageListAfterSent() {
        val fakeFlow = MutableStateFlow(ChatUiState(messageList = emptyList()))
        val mockViewModel = mockk<ChatViewModel>(relaxed = true)

        every { mockViewModel.uiState } returns fakeFlow
        every { mockViewModel.saveMessage(any(), any()) } answers {
            val msg = ChatMessage(firstArg(), secondArg(), Instant.now(), true, false)
            fakeFlow.value = fakeFlow.value.copy(
                messageList = fakeFlow.value.messageList + msg
            )
        }

        composeTestRule.setContent {
            ChatScreen(viewModel = mockViewModel, onBackClicked = {})
        }

        composeTestRule.onNodeWithTag("chat_input").performTextInput("Hello message")
        composeTestRule.onNodeWithTag("chat_input").assertTextEquals("Hello message")
        composeTestRule.onNodeWithTag("send_button").performClick()
        composeTestRule.onNodeWithTag("message_list").assert(hasAnyDescendant(hasText("Hello message")))
    }

    @Test
    fun sectionHeadersAreShown() {
        val t1 = Instant.parse("2025-01-02T10:00:00Z") // Thursday
        val fake = listOf(
            ChatMessage("Hey", false, t1, true, false)
        )

        val mockViewModel = mockk<ChatViewModel>()
        every { mockViewModel.uiState } returns
                MutableStateFlow(ChatUiState(messageList = fake))

        composeTestRule.setContent {
            ChatScreen(
                viewModel = mockViewModel,
                onBackClicked = {}
            )
        }

        composeTestRule.onNodeWithText("Thursday").assertExists()
        composeTestRule.onNodeWithText("10:00").assertExists()
    }

}