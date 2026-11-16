plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
android {
    namespace = "in.deven.scamsentry"
    compileSdk = 37
    defaultConfig {
        applicationId = "in.deven.scamsentry"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0-preview"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_21; targetCompatibility = JavaVersion.VERSION_21 }
    buildFeatures { compose = true; buildConfig = true }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    lint { abortOnError = true }
    sourceSets.getByName("androidTest").assets.srcDir("$projectDir/schemas")
}
ksp { arg("room.schemaLocation", "$projectDir/schemas") }
dependencies {
    implementation(project(":core-analysis"))
    implementation(libs.androidx.core)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.coroutines.tasks)
    implementation(libs.mlkit.text)
    implementation(libs.androidx.exif)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.room.testing)
    debugImplementation(libs.compose.test.manifest)
}
