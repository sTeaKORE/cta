# Combinatorial Testing Annotations

This library has the purpose of easing the initial threshold of getting into combinatorial testing, by
enabling the user to define an input model inside his own code base via annotations and providing a
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

This will clean the project, build it, run the annotation processing and then execute the tests.

### Additional commands

If you just want to run the annotation processing task you can run:

```
./gradlew clean kspTestKotlin
```

This will clean the project (to ensure changes are detected), build it and run the annotation processing.

If you just want to run the tests you can run:

```text
./gradlew test
```

Since we include the automatically generated tests in the test sources, we can just run the normal test command.
If you had no prior run this will also run the complete build command first. Afterwards it will only execute the tests.


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

Lastly ensure that you set up junit 5 tests correctly and your setup is complete.

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

The whole library is centered around annotations which can be seen in this table:

|    	Name   	    | Parameter                                                                                                                                                                                	   | Annotation Target |
|:---------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------:|
|   	CTATest 	    | **testContainer** - reference to class which implements test container interface<br/>**classToTest** - reference to classes which should be used as input model (currently only 1 supported) |   	Function  	    |
|   	CTADebug 	   | -                                                                                                                                                                                        	   |     	Any    	     |
|   CTABoolean	   | -                                                                                                                                                                                        	   |     	Any    	     |
|   	CTAEnum 	    | **values** - list of values for string enums                                                                                                                                             	   |     	Any    	     |
|   	CTAInt  	    | **from** - start of int range<br/> **to** - end of int range<br/> **values** - explicit list of int values                                                                               	   |     	Any    	     |
| CTAConstraints  | **ifConstraints** - list of if constraints                                                                                                                                               	   |    	Class   	     |
| CTAIfConstraint | **constraint** - constraint in string form                                                                                                                                               	   |     	Any    	     |

## CTADebug
This annotation simply enables if the application generates a debug logging file with useful debug logging.
This log can be found in

`build/generated/ksp/<main or test>/resources/*.log`

Example
```kotlin
@CTADebug
class Example {}
```

## CTATest
This annotation defines a combinatorial test case, meaning for each annotation one test file will be generated.
It needs 2 parameters:
* **classToTest** - List of classes used for generating the input model for the test. Currently only single class tests are supported. The referenced class is then annotated with value constraint and if constraints.
* **testContainer** - Reference to test container class which houses the oracle function which decides if a test is successful as well as a reference to the method which will be tested. The referenced class needs to implement the interface of CTATestContainer.kt.

Example
```kotlin
@CTATest(ContainerClass::class, ClassToTest::class)
fun testingClassA() {}
```

## Parameter Values (CTAInt, CTABoolean, CTAEnum)
This annotation is used to define and restrict input parameters of the input model and consist of the following variants.

### CTAInt
Annotation used to annotate integer parameters. These annotations can be used in 2 ways:

* **Int Range**
  * **from** - starting point of int range
  * **to** - end point of int range (range is each int between from and to)
* **Value List**
  * **values** - explicit list of integer values which are possible

Example Range
```kotlin
@CTAInt(from = 0, to = 10)
var rangeInt: Int = 1
```

Example List
```kotlin
@CTAInt(values = [0, 1, 2, 3, 4, 5])
var listInt: Int = 3
```

### CTABoolean
Annotation used to annotate boolean parameters. Boolean parameters only have true or false as possible values.

Example
```kotlin
@CTABoolean
var boolean: Boolean = false
```

### CTAEnum
Annotation used to annotate enum parameters. This annotation can be used to annotate enum classes or strings. Enum classes are automatically
parsed to collect all possible values. For strings, we need the additional **values** parameter to define the possible values of the string enum.

Note: Currently no inner class enums are supported.

Example Enum
```kotlin
@CTAEnum
var enum: Example = Example.Example1
```

Example String
```kotlin
@CTAEnum(values = ["RED", "GREEN", "YELLOW"])
var stringEnum: String = "RED"
```

## If Constraints (CTAConstraints, CTAIfConstraint)
This annotation is used to define if constraints, which describe and restrict the relationship and values of parameters.

### CTAConstraints
This annotation is more or less a wrapper which just collects all if constraints for a single class.
It holds a list of constraints within the **ifConstraints** parameter.

Example
```kotlin
@CTAConstraints(
    ...
)
class TestClass {}
```

### CTAIfConstraint
This annotation contains a single if constraint within the **constraint** parameter. It is a single string
written in the ACTS format (format of specific tool with limitations). You can read more about it here: 
https://csrc.nist.rip/groups/SNS/acts/documents/acts_user_guide_2.92.pdf

Example
```kotlin
@CTAConstraints(
    CTAIfConstraint("enum $CTEQUALS \"Example1\" $CTIMPLIES stringEnum $CTNOTEQUALS \"RED\""),
    CTAIfConstraint("rangeInt $CTEQUALS 2 $CTIMPLIES listInt $CTEQUALS 4"),
)
class TestClass {}
```
