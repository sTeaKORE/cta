package tuwien.cta.annotation.test

import kotlin.reflect.KFunction

/**
 * Interface for TestContainer.
 * A TestContainer holds a reference to the method which will be tested as well as the oracle function which will then
 * decide if the test was successful.
 */
interface CTATestContainer {

    var testMethod: KFunction<*>

    /**
     * Oracle Function which receives the function call and inputs for a singular test run as input and returns the
     * success of the test run as boolean as output.
     * @param methodToTest function call for test run.
     * @param inputs input for test run
     * @return if test run was successful
     */
    fun oracle(methodToTest: KFunction<*>, inputs: Array<Any>): Boolean
}
