plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}
android {
    namespace = "com.example.androidplayistcreator"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.androidplayistcreator"
        minSdk = 28
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("META-INF/DEPENDENCIES")
        }
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx) // Pour lifecycleScope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.glide)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.exoplayer)
    implementation(libs.room)
    implementation(libs.roomKtx)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.room.runtime.v261)
    ksp(libs.androidx.room.compiler.v261)
}