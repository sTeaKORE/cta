val kspVersion: String by project

plugins {
    kotlin("jvm")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:javapoet:1.12.1")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.4.0")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

