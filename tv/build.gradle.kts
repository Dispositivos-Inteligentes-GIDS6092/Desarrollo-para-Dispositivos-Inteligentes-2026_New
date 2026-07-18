plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

android {
    namespace = "mx.utng.smarthealthmonitor.tv"
    compileSdk = 36
    
    defaultConfig {
        applicationId = "mx.edu.utng.bgma.smarthealthmonitor"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Compose for TV - Material 3
    implementation("androidx.tv:tv-material:1.0.0-rc02")
    implementation("androidx.tv:tv-foundation:1.0.0-alpha11")
    
    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.7")
    
    implementation(project(":shared"))
    
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation(libs.androidx.material3)

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    val media3Version = "1.4.1"
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-ui:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")
    
    implementation(libs.glide)
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    implementation(libs.play.services.wearable)
    implementation(libs.play.services.cast.framework)
    implementation("androidx.mediarouter:mediarouter:1.7.0")
}
