package com.example.muzzchat

import app.cash.turbine.test
import com.example.muzzchat.domain.model.ChatMessage
import com.example.muzzchat.domain.repository.ChatRepository
import com.example.muzzchat.ui.presentation.ChatViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class ChatViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test that section flags are set correctly`() = runTest {
        val t1 = Instant.now()
        val t2 = t1.plusSeconds(60*60+10)
        val t3 = t2.plusSeconds(10)

        val repo: ChatRepository = mockk()

        coEvery { repo.getMessages() } returns flowOf(
            listOf(
                ChatMessage("Message 1", false, t1, false, false),
                ChatMessage("Message 2", false, t2, false, false),
                ChatMessage("Message 3", false, t3, false, false)
            )
        )

        val viewModel = ChatViewModel(repo)

        viewModel.loadMessages()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            val list = state.messageList

            assertEquals(3, list.size)
            assertTrue(list[0].isSectionRequired) //First message always have section
            assertTrue(list[1].isSectionRequired) //Over 60s should have a section too
            assertFalse(list[2].isSectionRequired) //10s so NO section

            cancelAndIgnoreRemainingEvents()
        }
    }

}