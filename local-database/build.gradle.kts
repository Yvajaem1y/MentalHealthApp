plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias { libs.plugins.ksp }
}

android {
    namespace = "com.hackhathon.local_database"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        consumerProguardFiles("consumer-rules.pro")
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

    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.gson)
}