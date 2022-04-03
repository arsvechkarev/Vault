package buisnesslogic.random

/**
 * Random generator with seed
 */
interface SeedRandomGenerator {

    /**
     * Generates a random int in the range 0..[limit] based on [seed]
     */
    fun generateNumber(seed: ByteArray, limit: Int): Int
}