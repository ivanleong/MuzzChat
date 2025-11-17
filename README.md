
# Muzz Chat App

This app features a chat interface with a message list and an input text to display all entered texts. All messages sent by the user is displayed as grey bubbles and recipients' as pink bubbles. The action bar includes a menu item to allow the toggling between the user and recipient to mock a 2 sided conversation. 

Since the design of the message screen included a back navigation on the AppBar, a dummy main screen was also implemented to simulate the back action from the message screen and also allowing to show that the messages persisted without closing the app.



## Screenshots

![App Screenshot](https://raw.githubusercontent.com/ivanleong/MuzzChat/refs/heads/main/screenshots/Screenshot_2.png)


## Project Structure

```bash
  MuzzChatApp
    ├── src
    │   ├── main 
    │   │   ├── data 
    │   │   │   ├── local 
    │   │   │   │   ├── ChatDatabase.kt
    │   │   │   │   ├── ChatMessageDao.kt
    │   │   │   │   ├── ChatMessageEntity.kt
    │   │   │   ├── repository 
    │   │   │   │   ├── ChatRepositoryImpl.kt
    │   │   ├── di 
    │   │   │   ├── AppModule.kt 
    │   │   ├── domain 
    │   │   │   ├── model
    │   │   │   │   ├── ChatMessage.kt
    │   │   │   ├── repository
    │   │   │   │   ├── ChatMessage.kt
    │   │   ├── ui 
    │   │   │   ├── components
    │   │   │   │   ├── AppBar.kt
    │   │   │   ├── presentation
    │   │   │   │   ├── ChatScreen.kt
    │   │   │   │   ├── ChatViewModel.kt
    │   │   │   │   ├── MainNav.kt
    │   │   │   │   ├── MainScreen.kt
    │   │   │   ├── theme
    │   │   │   │   ├── Color.kt
    │   │   │   │   ├── Theme.kt
    │   │   │   │   ├── Type.kt
    │   │   ├── MuzzChatApplication.kt
    │   │   ├── MainActivity.kt
    │   ├── androidTest
    │   │   ├── ChatViewModelTest.kt
    │   ├── test
    │   │   ├── ChatViewModelTest.kt
    │   │   ├── MainDispatcherRule.kt
    ├── build.gradle.kts

```
## Technical Decisions

#### Architecure 

MVVM with SOLID principles.

#### Tech stack and Libraries

- Jetpack Compose – UI framework
- Room – Used for persistent storage
- Hilt - Dependency injection.
- Coroutines + Flow for asynchronous and reactive data handling from database to UI views
## Testing Strategy

- **Unit Tests:** Validate ViewModel logic using JUnit, MockK, and Turbine. A test was added to test the processSectionsAndUpdate() function when new messages are loaded. This ensures that the correct section header flags are correctly showed for the approprioate messages,

- **Instrumented UI Test:** Three tests were added to 1. check that messages added to the messageList in the viewModel are in fact displayed in the message list screen. 2. messages typed in input box are shown in the input box and shown in the message list after the send button is clicked. 3. messages whos isSectionRequired flag marked as true shows a section header in the message list. 
## How to run

1. Clone the repo

```bash
git clone https://github.com/ivanleong/MuzzChatApp
cd MuzzChatApp
```

2. Open the project in Android Studio

3. Build and Run the app on a physical device or emulator
## App limitation and improvments

1. The main app logic for processing the section headers currently sits in the viewmodel. This could be isolated in a usecase so that it can be tested in isolation for improved test coverage

2. At the moment each time a new message is added to the list, the entire message list is proceesed to set their isSectionRequired flags. This can be very expensive for very long list of messages. To improve this we can update it so that only the last message is processed since we know that the current list is already set. We also save the isSectionRequired flag in the database.

3. The ChatScreen() composable currently has a hiltViewModel() dependency which makes testing tricky. This could be moved to the NavGraph composable so that ChatScreen could be better tested in isolation.

4. The ChatScreen currently does not have a loading or error state as we assume fetching from database is instantaneous. Given more time these 2 states could be added for more UI resilience.

5. Not a limitation but given more time the colours used in the apps could be added to the colour theme schemas instead of referencing straight from the resource files.

