import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.secrets.gradle.plugin)
    id("kotlin-parcelize")
}

android {
    namespace = "com.novandi.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 22

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val keystoreFile = project.rootProject.file("baseurl.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        val journeyUrl = properties.getProperty("JOURNEY_URL") ?: ""
        val mlUrl = properties.getProperty("ML_URL") ?: ""
        val whatsappUrl = properties.getProperty("WHATSAPP_URL") ?: ""
        val regencyUrl = properties.getProperty("REGENCY_URL") ?: ""
        val whatsappAppKey = properties.getProperty("WHATSAPP_APP_KEY") ?: ""
        val whatsappAuthKey = properties.getProperty("WHATSAPP_AUTH_KEY") ?: ""

        buildConfigField("String", "JOURNEY_URL", journeyUrl)
        buildConfigField("String", "ML_URL", mlUrl)
        buildConfigField("String", "WHATSAPP_URL", whatsappUrl)
        buildConfigField("String", "REGENCY_URL", regencyUrl)
        buildConfigField("String", "WHATSAPP_APP_KEY", whatsappAppKey)
        buildConfigField("String", "WHATSAPP_AUTH_KEY", whatsappAuthKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":utility"))

    // Hilt
    implementation(libs.dagger.hilt.android)
    implementation(libs.firebase.messaging)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.dagger.compiler)
    implementation(libs.androidx.hilt.navigation)

    // Retrofit
    api(libs.retrofit)
    api(libs.retrofit.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Paging 3
    implementation(libs.androidx.pager)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.compose)
    implementation(libs.room.paging)

    // DataStore
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.core)

    // Room
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)

    // Work Manager
    implementation(libs.work.manager)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.core.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}