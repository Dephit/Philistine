package com.sergeenko.alexey.noble

import java.io.Serializable

data class Muscle(
    var isMuscleSelected: Boolean = false,
    var muscleName: String = "",
    var musclePower: Int = 0
): Serializable