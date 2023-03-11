package tuwien.cta.annotation.test

import kotlin.reflect.KClass

/**
 * Combinatorial Testing Annotation.
 * This annotation defines one combinatorial test, which will automatically generate a test.
 * This annotation needs a test container and an arbitrary amount of classes which are used for generating an
 * input model.
 * Although an arbitrary amount of classes can be defined, currently only singular class tests are supported.
 *
 * Example
 * ```
 * @CTATest(ContainerClass::class, ClassToTest::class)
 * fun testingClassA() {}
 * ```
 */
@Target(allowedTargets = [AnnotationTarget.FUNCTION])
annotation class CTATest(val testContainer: KClass<out CTATestContainer>, vararg val classToTest: KClass<*>)
