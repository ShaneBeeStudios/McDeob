plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.0"
    id("edu.sc.seis.macAppBundle") version "2.3.1"
}

// Version of McDeob
val projectVersion = "3.3.0"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/public/")
    maven("https://jitpack.io")
    maven("https://repo.maven.apache.org/maven2/")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.1.0")
    implementation("org.json:json:20210307") {
        isTransitive = false
    }
    implementation("net.md-5:SpecialSource:1.11.5-SNAPSHOT") {
        //exclude("com.google.guava")
        exclude("com.opencsv")
    }
    implementation("io.papermc:patched-spigot-fernflower:0.1+build.13") {
        isTransitive = false
    }
}

tasks {
    compileJava {
        options.release = 21
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
    }
    shadowJar {
        archiveFileName = project.name + "-" + projectVersion + ".jar"
        manifest.attributes["Main-Class"] = "com.shanebeestudios.mcdeob.McDeob"
    }
    jar {
        dependsOn(shadowJar)
    }
    createDmg {
        dependsOn(jar)
    }
}

macAppBundle {
    mainClassName = "com.shanebeestudios.mcdeob.McDeob"
    icon = "src/main/resources/images/1024.icns"
    runtimeConfigurationName = "shadow"
    jarTask = "shadowJar"
    appOutputDir = "libs"
    dmgOutputDir = "libs"
    dmgName = "McDeob-$projectVersion-macOS"
    bundleExtras["CFBundleVersion"] = projectVersion
    bundleExtras["CFBundleShortVersionString"] = "Version"
    bundleExtras["NSRequiresAquaSystemAppearance"] = false
    javaExtras["-Xms3G"] = null
}
