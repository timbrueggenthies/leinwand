plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "de.brueggenthies.leinwand.core"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        aarMetadata {
            minCompileSdk = 21
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.compose.ui)
    api(libs.media3.exoplayer)
}

publishing {
    publications {
        // Creates a Maven publication called "release".
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = "core"
            version = "1.0.0-alpha01"
            afterEvaluate {
                from(components["release"])
            }
        }
    }
}