plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.apprememberit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.apprememberit"
        minSdk = 30
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

    // Asegurarse de usar la versión correcta del compilador de Compose
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()  // Versión del compilador de Compose definida en `libs.versions.toml`
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core AndroidX y Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose (usando BOM para gestionar versiones de Compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // AppCompat y Material
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Activity y ConstraintLayout para Compose
    implementation(libs.androidx.activity)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")  // Asegura la versión de ConstraintLayout para Compose

    // Firebase (Authentication y Database)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth.ktx)

    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    // Debug tools para Jetpack Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing (JUnit y Espresso)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose específico (consolidado con BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.runtime:runtime-saveable")

    // Navigation y ViewModel en Compose
    implementation("androidx.navigation:navigation-compose:2.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    // Gson para manejo de JSON
    implementation(libs.gson)
}

