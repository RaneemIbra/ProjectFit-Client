import com.android.build.api.dsl.Lint
import com.android.build.api.dsl.LintOptions

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.projectfit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projectfit"
        minSdk = 21
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lint {
        abortOnError = false
        disable.add("NotificationPermission")
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.lzyzsd:circleprogress:1.2.1")
    api("com.google.android.material:material:1.12.0-alpha01")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Add this line for Mockito
    testImplementation ("org.mockito:mockito-core:5.3.1")
    // For Android-specific Mockito features, you can also include this:
    androidTestImplementation ("org.mockito:mockito-android:5.3.1")
    // You can also include Espresso for UI testing (optional)
    //may delete this
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Add the AndroidX Test dependencies

    androidTestImplementation ("androidx.test:core:1.4.0")
    androidTestImplementation ("androidx.test:core-ktx:1.4.0")
}