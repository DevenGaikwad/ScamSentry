plugins { alias(libs.plugins.kotlin.jvm) }
kotlin { jvmToolchain(21) }
dependencies { compileOnly(libs.json); testImplementation(libs.json); testImplementation(libs.junit) }
tasks.test {
    systemProperty("model.dir", rootProject.file("app/src/main/assets/model").absolutePath)
    systemProperty("parity.dir", rootProject.file("../ml/artifacts/parity").absolutePath)
    testLogging { events("passed", "skipped", "failed") }
}
