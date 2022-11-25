package buisnesslogic.generator

fun interface RandomGenerator {
  
  fun nextInt(bound: Int): Int
}