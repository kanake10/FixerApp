plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kover)
}

android {
    namespace = "com.example.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp("androidx.room:room-compiler:2.6.1")
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.robolectric)
    api(libs.junit4)
    api(libs.junit4.ext)
    api(libs.turbine)
    api(libs.mock.android)
    api(libs.mock.agent)
    api(libs.google.truth)
    api(libs.coroutines.test)
    api(libs.espresso.core)
    api(libs.androidx.arch.core.testing)
}
