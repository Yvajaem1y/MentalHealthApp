plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.hackhathon.domain"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
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
}

dependencies {
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}