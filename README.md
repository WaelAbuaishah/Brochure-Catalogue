# 📚 Brochure Catalogue

Welcome to **Brochure Catalogue**, an Android application built with modern development principles, supporting **Android 8.0+** (API 26+) 🎉, covering **95.4% of devices worldwide 🌍**.

---

## 🌟 Key Highlights

- 📱 **Android 8+** compatibility (API 26+), ensuring wide device support.
- ⚡ **Java 17 compatibility**: `sourceCompatibility = JavaVersion.VERSION_17` for modern Java features.
- 🛠️ **Layer-Based Modularization**:
    - **App (UI Layer)**: Manages all UI logic using Jetpack Compose.
    - **Domain**: Contains use cases and business logic.
    - **Data**: Handles local and remote data sources.

- 📦 **Centralized Dependency Management**:
    - Dependencies are organized using **Gradle Catalogs** for easy maintenance.

---

## 🚀 How to Run the Project

1. Open the project in **Android Studio** (Arctic Fox or higher recommended).
2. Build the project using the `Sync Gradle` option.
3. Run the app on a connected device or emulator.

Enjoy! 🎉

---

## 📚 Technologies & Libraries Used

- 🧩 **Moshi**: For JSON parsing and serialization.
- 🌐 **Retrofit**: To interact with REST APIs seamlessly.
- 🎨 **Jetpack Compose**: For creating modern, reactive UIs.
- 🧪 **Hilt**: For dependency injection and modularity.
- 💾 **Room**: For efficient local data storage.
- 🧪 **Testing Frameworks**:
    - **JUnit4**: Unit testing framework.
    - **Robolectric**: For testing Android components.
    - **Room Database Testing**: To validate database functionality.
    - **MockWebServer**: To mock API responses.

---

## 🎯 Features

- 🖼️ **Brochure Listing**:
    - Displays a list of brochures with images and retailer names using **Compose**.
    - Fallback to a **placeholder image** for brochures without images.
    - Pulls data from the source: [API data source](https://mobile-s3-test-assets.aws-sdlc-bonial.com/shelf.json).

- 🧹 **Content Filtering**:
    - Displays brochures with `contentType = "brochure"` or `contentType = "brochurePremium"`.
    - Filters brochures closer than **5km** with an **interactive slider**.

- 🎨 **Dynamic Grid Layout**:
    - Portrait Mode: **2-column grid**.
    - Landscape Mode: **3-column grid**.
    - Premium brochures take up **full width** across all columns.

---

## 🖼️ Screenshots

| Dark Mode (With Filter)            | Dark Mode (Without Filter)                                                                                                                                                           |
|---------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ![Dark With Filter](https://firebasestorage.googleapis.com/v0/b/sawadevelopmentandaid.appspot.com/o/dark_with_filter.png?alt=media&token=562dfed1-6097-4c05-ac25-6c9351aad21b) | ![Dark Without Filter](https://firebasestorage.googleapis.com/v0/b/sawadevelopmentandaid.appspot.com/o/dark_without_filter.png?alt=media&token=5e970533-197d-4c3e-ae64-50814b633cdc) |

| Light Mode (With Filter)           | Light Mode (Without Filter)                                                                                                                                                             |
|---------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ![Light With Filter](https://firebasestorage.googleapis.com/v0/b/sawadevelopmentandaid.appspot.com/o/light%20with%20filter.png?alt=media&token=cd43ce28-82ca-41b7-82d3-25691778fed9) | ![Light Without~~~~ Filter](https://firebasestorage.googleapis.com/v0/b/sawadevelopmentandaid.appspot.com/o/light_without_filter.png?alt=media&token=c0bba109-296d-4d8d-b2ad-85a7790c6173~~~~) |

---

Happy coding! 🚀
~~~~