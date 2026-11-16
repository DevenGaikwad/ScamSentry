plugins { alias(libs.plugins.android.test) }
android {
    namespace = "in.deven.scamsentry.benchmark"
    compileSdk = 37
    defaultConfig { minSdk = 26; targetSdk = 36; testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
    targetProjectPath = ":app"
    buildTypes {
        create("benchmark") { isDebuggable = true; signingConfig = signingConfigs.getByName("debug"); matchingFallbacks += "release" }
    }
    experimentalProperties["android.experimental.self-instrumenting"] = true
    compileOptions { sourceCompatibility = JavaVersion.VERSION_21; targetCompatibility = JavaVersion.VERSION_21 }
}
dependencies {
    implementation(libs.macrobenchmark)
    implementation(libs.uiautomator)
    implementation(libs.androidx.test.ext)
    implementation(libs.androidx.test.runner)
}
