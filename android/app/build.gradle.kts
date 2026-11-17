plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
android {
    namespace = "in.deven.scamsentry"
    compileSdk = 37
    testBuildType = providers.gradleProperty("scamsentry.instrumentedBuild").getOrElse("debug")
    defaultConfig {
        applicationId = "in.deven.scamsentry"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "0.2.0-preview"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("benchmark") {
            initWith(getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += "release"
            proguardFile("benchmark-proguard-rules.pro")
            testProguardFile("test-proguard-rules.pro")
        }
    }
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a", "x86_64")
            isUniversalApk = true
        }
    }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_21; targetCompatibility = JavaVersion.VERSION_21 }
    buildFeatures { compose = true; buildConfig = true }
    packaging { resources.excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    lint { abortOnError = true }
    sourceSets.getByName("androidTest").assets.directories.add("$projectDir/schemas")
}
ksp { arg("room.schemaLocation", "$projectDir/schemas") }
dependencies {
    // Align app + instrumentation runtimes with Room's generated serializers.
    implementation(platform(libs.kotlinx.serialization.bom))
    androidTestImplementation(platform(libs.kotlinx.serialization.bom))
    implementation(project(":core-analysis"))
    implementation(libs.androidx.core)
    implementation(libs.profileinstaller)
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
    implementation(libs.compose.window)
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.room.testing)
    // 2.46 removes deprecated javax.lang.model enum references absent on Android.
    androidTestImplementation("com.google.errorprone:error_prone_annotations:2.46.0")
    debugImplementation(libs.compose.test.manifest)
}
