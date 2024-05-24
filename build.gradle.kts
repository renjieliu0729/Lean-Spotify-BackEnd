val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotlin_serialization: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
    kotlin("plugin.serialization") version "1.8.10"
}

group = "com.laioffer"
version = "0.0.1"
application {
    mainClass.set("com.laioffer.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        jvmToolchain(11)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_serialization")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}