val kspVersion: String by project
val kotlinVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka") version "1.7.20"
}

group = "tuwien.cta"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.4.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

