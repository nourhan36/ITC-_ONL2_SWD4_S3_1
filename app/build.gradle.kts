plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.itc__onl2_swd4_s3_1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.itc__onl2_swd4_s3_1"
        minSdk = 26
        targetSdk = 34
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjvm-default=compatibility")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation ("androidx.navigation:navigation-compose:2.7.6")
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    val room_version = "2.7.0"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    implementation (libs.firebase.firestore.ktx)
    ksp("androidx.room:room-compiler:$room_version")
    implementation(libs.firebase.firestore)
    testImplementation("androidx.room:room-testing:$room_version")
    androidTestImplementation("androidx.room:room-testing:$room_version")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    //===============================

    // coil
    implementation(libs.coil.compose)
    // navigation
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.mockk)
    testImplementation(libs.turbine)

    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.ktx)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.coroutines)
    implementation(libs.retrofit)
    implementation(libs.okhttp3)
    implementation(libs.gson)
    implementation(libs.gson.converter)


    implementation(libs.datastore)
    implementation(libs.lottie.compose)

    implementation(libs.gson.converter) // Or the latest version
    implementation(libs.retrofit) // Make sure you have the retrofit dependency
    implementation(libs.okhttp.v4110) // Make sure you have the okhttp dependency

    implementation (libs.retrofit)
    implementation (libs.gson.converter)

}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask> {
    kotlinOptions {
        jvmTarget = "1.8"
    }

}