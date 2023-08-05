package viewdsl

val Int.dp: Int get() = (Densities.density * this).toInt()

val Int.sp: Float get() = Densities.scaledDensity * this
