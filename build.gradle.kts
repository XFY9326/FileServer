@file:Suppress("PropertyName")

val kotlin_version: String by project
val ktor_version: String by project
val clikt_version: String by project
val kotlinx_coroutines_version: String by project
val kotlinx_serialization_version: String by project
val logback_version: String by project
val jansi_version: String by project

plugins {
    application
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
}

group = "tool.xfy9326"
version = "0.0.10"

val MainClass = "tool.xfy9326.fileserver.ApplicationKt"
val Author = "XFY9326"

application {
    mainClass.set(MainClass)
}

tasks.register<Jar>("assembleJar") {
    dependsOn("jar")

    manifest {
        attributes["Main-Class"] = MainClass
        attributes["Specification-Title"] = project.name
        attributes["Specification-Version"] = project.version
        attributes["Specification-Vendor"] = Author
    }

    destinationDirectory.set(File(project.buildDir, "distributions"))
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(tasks.compileJava.get().destinationDirectory.get())
    from(tasks.compileKotlin.get().destinationDirectory.get())
    from(tasks.processResources.get().destinationDir)
    from(
        configurations.compileClasspath.get().map {
            if (it.isDirectory) it else zipTree(it)
        }
    )
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

tasks.compileTestKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinx_serialization_version")

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-cio:$ktor_version")
    implementation("io.ktor:ktor-server-html-builder:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging:$ktor_version")
    implementation("io.ktor:ktor-server-caching-headers:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.fusesource.jansi:jansi:$jansi_version")

    implementation("com.github.ajalt.clikt:clikt:$clikt_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
}