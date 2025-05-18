plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id ("kotlin-kapt")
    id("dagger.hilt.android.plugin") // 🔥 أضيفي هذا السطر
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
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

        implementation ("com.google.accompanist:accompanist-pager:0.30.1")
        implementation ("com.google.accompanist:accompanist-pager-indicators:0.30.1")
// Hilt Core
    implementation("com.google.dagger:hilt-android:2.51.1")
// Hilt Compiler
    kapt("com.google.dagger:hilt-compiler:2.51.1")
// لو كنتِ تستخدمين Hilt مع ViewModel
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    implementation ("androidx.work:work-runtime-ktx:2.8.1")
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
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.generativeai)
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
    kapt("androidx.room:room-compiler:$room_version")


}