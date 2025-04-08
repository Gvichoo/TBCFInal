plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //Hilt
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    //Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.tbacademy.nextstep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tbacademy.nextstep"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.android)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Retrofit
    implementation(libs.retrofit)
    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    //Coroutine
    implementation(libs.kotlinx.coroutines.android)
    //Glide
    implementation (libs.glide)
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    //FireStore
    implementation("com.google.firebase:firebase-firestore")
    //DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.1.4")
    //FirebaseGoogle
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation ("com.google.firebase:firebase-auth-ktx:23.2.0")
    // Google Identity Services
    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))





}