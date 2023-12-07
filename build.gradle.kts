import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "tool.xfy9326"
version = "0.0.13"

tasks.withType<Jar> {
    manifest {
        attributes(
            "Main-Class" to "tool.xfy9326.fileserver.ApplicationKt",
            "Version" to version,
        )
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("with-dependencies")

    exclude(
        "LICENSE",
        "DebugProbesKt.bin",
        "META-INF/com.android.tools/**",
        "META-INF/maven/**",
        "META-INF/proguard/**",
        "META-INF/*.version",
        "META-INF/LICENSE",
        "META-INF/LICENSE.txt",
        "META-INF/LGPL2.1",
        "META-INF/AL2.0",
    )

    mergeServiceFiles()
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")

    val ktorVersion = "2.3.6"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-auth:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")
    implementation("io.ktor:ktor-server-partial-content:$ktorVersion")
    implementation("io.ktor:ktor-server-auto-head-response:$ktorVersion")

    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("org.fusesource.jansi:jansi:2.4.1")

    implementation("com.github.ajalt.clikt:clikt:4.2.1")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation(kotlin("test"))
}
