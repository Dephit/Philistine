package com.sergeenko.alexey.noble.dataclasses

import java.io.Serializable

data class Params(
        var imt: Int? = null,
        var fatTisue: Int? = null,
        var muscleMass: Int? = null,
        var waterInBody: Int? = null,
        var weight: Int? = null
): Serializable {

    fun isEmpty() = imt == null &&
            fatTisue == null &&
            muscleMass == null &&
            waterInBody == null &&
            weight == null
}