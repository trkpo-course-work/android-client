plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("jacoco")
}

android {
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "ru.spbstu.blog"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(project(mapOf("path" to ":common")))
    implementation(project(mapOf("path" to ":auth")))
    implementation(project(mapOf("path" to ":wall")))
    implementation(project(mapOf("path" to ":profile")))
    implementation(project(mapOf("path" to ":search")))
    implementation(project(mapOf("path" to ":diary")))
    implementation(Deps.appLibraries)
    implementation(Deps.daggerDep)
    implementation(Deps.network)
    kapt(Deps.daggerKapt)
    testImplementation(Deps.testLibraries)
    androidTestImplementation(Deps.androidTestLibraries)
}