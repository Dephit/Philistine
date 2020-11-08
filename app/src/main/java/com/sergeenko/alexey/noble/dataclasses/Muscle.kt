package com.sergeenko.alexey.noble.dataclasses

import java.io.Serializable

data class Muscle(
    var isMuscleSelected: Boolean = false,
    var muscleName: String = "",
    var musclePower: Int = 0
): Serializable