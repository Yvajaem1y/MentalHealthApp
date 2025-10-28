plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.hackhathon.data"
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

    implementation(project(":yandex-gpt-api"))
    implementation(project(":local-database"))

    implementation(libs.androidx.core.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)

    //DI
    implementation(libs.jakarta.inject)
}