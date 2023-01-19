# Combinatorial Testing Annotations

This library has the purpose of easing the initial threshold of getting into combinatorial testing, by 
enabling the user to define an input model inside his own code base via annotations and providing an
completely automated process which converts defined input model to running junit 5 tests.

This repository consists of 2 components: cta-processor and demo.

Cta-processor is the component which does all the work, while demo is a sample project using the cta-processor
and its functionality as a showcase.

# Pre-Requisites

This library:
* Is made for kotlin projects
* Needs junit 5 for running generated tests
* Needs gradle

# Setup

## Sample Setup

If you just want to run the demo project to check out sample functionality you just have to clone this
repository and run:
```
./gradlew clean build
```

or if you just want to run the annotation processing task you can run:

```
./gradlew clean kspTestKotlin
```

## Custom Setup

If you want to use the library in a different project you need to do the following steps:

Currently, the process to integrating a library is by directly referencing it in the same gradle project as seen
in demo and directly taken from ksp documentation

build.gradle.kts
```kotlin
plugins {
    id("com.google.devtools.ksp") version "1.8.0-1.0.8"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":<name-of-library-in-gradle>"))
    ksp(project(":<name-of-library-in-gradle>"))
}
```
If there ever arises the need to pass parameters to ksp you can do that by adding

build.gradle.kts
```kotlin
ksp {
    arg("option1", "value1")
    arg("option2", "value2")
}
```

The next step is to add the generated directories from ksp to sources and test sources. This ensures that
the automatically generated tests are found during the test gradle task.

build.gradle.kts
```kotlin
idea {
    module {
        // Not using += due to https://github.com/gradle/gradle/issues/8749
        sourceDirs = sourceDirs + file("build/generated/ksp/main/kotlin") // or tasks["kspKotlin"].destination
        testSourceDirs = testSourceDirs + file("build/generated/ksp/test/kotlin")
        generatedSourceDirs = generatedSourceDirs + file("build/generated/ksp/main/kotlin") + file("build/generated/ksp/test/kotlin")
    }
}
```

Lastly ensure that you setup junit 5 tests correctly and your setup is complete.

build.gradle.kts
```kotlin
tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
```

# Documentation
