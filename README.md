# Currency Calculator App

This is a Kotlin-based currency converter application built using Jetpack Compose. The project implements a clean and modular architecture with the MVVM pattern. The app fetches conversion rates from the [Fixer.io](https://fixer.io) api.
### Features:
- Convert currencies in real-time using the Fixer.io API.
- UI built with Jetpack Compose and follows Material3 design guidelines.
- Error handling for HTTP requests and offline functionality.
- Caching using Room to allow for offline usage.
- ViewModel manages UI state and business logic.
- Hilt for dependency injection.

## Project Structure/Modules Overview:

1. **:app**:
   - This is the main module that contains the Android application code and the entry point for the app.
   - It includes the UI code for the currency converter and is responsible for initiating the app's core functionality.
   - It also contains UI tests for various composables such as `CurrencyTopBar`
   - 

2. **:core**:
   - Contains shared code used across multiple modules such as utility classes, constants and common resources.

3. **:libraries:network**:
   - This module encapsulates network-related functionalities, including API calls using Retrofit.
   - It is responsible for fetching data from the Fixer.io API for currency conversion rates.

4. **:libraries:designsystem**:
   - Contains custom UI components and styles that follow Material3 design principles.
   - This module handles the reusable UI elements used throughout the app, like buttons and text fields etc.

5. **:libraries:testing**:
   - Contains testing utilities and mock data for unit tests.
   - This module provides tools like Mockk for mocking dependencies and testing components.

6. **:features:currency**:
   - This module contains the `CurrencyRepo` interface, which acts as a contract for the `:features:currencyimpl` module.

7. **:features:currencyimpl**:
   - This module contains the implementation for currency conversion and related data handling.
   - It communicates with the `:libraries:network` module to fetch the conversion rates and stores them locally using Room.

## Technologies Used

## Technologies Used

- **[Kotlin](https://kotlinlang.org/)**: The primary programming language.
- **[Jetpack Compose](https://developer.android.com/jetpack/compose)**: For building the UI.
- **[Hilt](https://developer.android.com/training/dependency-injection/hilt-android)**: Dependency injection framework.
- **[Retrofit](https://square.github.io/retrofit/)**: For making HTTP requests to [Fixer.io](https://fixer.io) for currency conversion rates.
- **[Room](https://developer.android.com/topic/libraries/architecture/room)**: For offline caching of currency data.
- **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)**: For handling asynchronous operations.
- **[Material3](https://m3.material.io/)**: For adhering to Material design guidelines.
- **[Mockk](https://mockk.io/)**: A mocking library for Kotlin, used for unit testing.
- **[Google Truth](https://truth.dev/)**: A library for fluent assertions in tests.
- **[JUnit](https://junit.org/junit5/)**: A framework for unit testing in Java/Kotlin.
- **[Flows](https://kotlinlang.org/docs/flow.html)**: For handling asynchronous streams of data in a non-blocking way.
- **[Coil](https://coil-kt.github.io/coil/)**: An image loading library for Android that supports both caching and image transformation.


## Testing

This project includes several levels of tests to ensure proper functionality and stability:

### 1. **DAO Tests**:
   - Test the local Room database interactions, ensuring data is stored and retrieved correctly.
   - Includes tests for the currency data cache.

### 2. **Repository Implementation Tests**:
   - Verify that the repository interacts with both remote and local data sources.

### 3. **ViewModel Tests**:
   - Ensure that the ViewModel correctly handles UI states like loading, success and error.
   - Test the business logic, including currency conversion calculations and error handling.

### 4. **Currency Component Tests**:
   - Verify individual UI components in the currency converter screen, such as text fields, buttons, and dropdowns.
   - Test that the UI responds to state changes properly.

### 5. **UI Tests**:
   - Ensure the UI displays correctly according to the design mockups.
   - Tests for user interactions such as clicking the "Convert" button, selecting currencies, and verifying the converted amount.

## Static Code Analysis

- **Detekt** and **Ktlint** are integrated to ensure clean and maintainable code.
  - **Detekt**: Analyzes the Kotlin code for potential issues such as code smells, complexity, and style violations.
  - **Ktlint**: Enforces consistent code formatting following Kotlin style guidelines.
 
## Ui Preview

The device used for testing is Google Pixel 6A
 
| Home | CurrenyList | CurrencySearch |
| --- | --- | --- |
| ![Email](https://github.com/kanake10/FixerApp/blob/main/screenshots/home.png) | ![Location](https://github.com/kanake10/FixerApp/blob/main/screenshots/currencylist.png) | ![Signin](https://github.com/kanake10/FixerApp/blob/main/screenshots/search.png) |

