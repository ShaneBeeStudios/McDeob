import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    application
    java
    id("io.freefair.lombok") version "8.4"
    id("com.diffplug.spotless") version "6.22.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.kenzie.mx/releases")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("mx.kenzie:mirror:5.0.5")
    implementation("io.github.lxgaming:reconstruct-common:1.3.24")
    implementation("org.vineflower:vineflower:1.9.3")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass = "com.shanebeestudios.mcdeop.McDeob"
}

group = "com.shanebeestudios"
// x-release-please-start-version
version = "2.6.0"
// x-release-please-end
description = "McDeob"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

spotless {
    java {
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()

        // Cleanthat will refactor your code, but it may break your style: apply it before your formatter
        cleanthat()

        palantirJavaFormat()

        formatAnnotations() // fixes formatting of type annotations
    }

    kotlinGradle {
        ktlint()
    }

    yaml {
        target("*.yaml")
        jackson()
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")

    dependsOn("distTar", "distZip")
}