plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.hackhathon.yandex_gpt_api"
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
    implementation(libs.androidx.core.ktx)

    //Retrofit
    implementation(libs.retrofit.adapters.result)
    implementation(libs.retrofit)
    implementation(libs.jakewharton.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logging.interceptor)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)

    //DI
    implementation(libs.androidx.annotation)

}