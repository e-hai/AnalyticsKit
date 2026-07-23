plugins {
    alias(libs.plugins.android.library)
}

apply(from = "${rootDir}/gradle/android-library-publish.gradle.kts")

android {
    namespace = "com.kit.analytics.firebase"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(project(":analytics"))
    // BOM must be api so consumers resolve firebase-* with a version.
    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
}
