plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.hackhathon.mentalhealthapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hackhathon.mentalhealthapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"AQVNxiWGkP_Tyh-SRBNxZ7KHxaOxLSA_vI5OAwBP\"")
        buildConfigField("String", "FOLDER_ID", "\"b1g3h6r0glrjdddh24gc\"")
    }

    packagingOptions {
        resources.excludes.add("META-INF/versions/9/OSGI-INF/MANIFEST.MF")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig=true
    }
}

dependencies {
    implementation(project(":yandex-gpt-api"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":local-database"))
    implementation(project(":ui-kit"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.bumptech.glide.compose)
    implementation(libs.bumptech.glide)

    implementation(libs.androidx.navigation.compose)

    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("com.google.accompanist:accompanist-pager:0.28.0")
}