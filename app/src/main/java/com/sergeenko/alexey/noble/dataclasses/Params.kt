package com.sergeenko.alexey.noble.dataclasses

data class Params(
        var imt: Int? = null,
        var fatTisue: Int? = null,
        var muscleMass: Int? = null,
        var waterInBody: Int? = null,
        var weight: Int? = null
){

    fun isEmpty() = imt == null &&
            fatTisue == null &&
            muscleMass == null &&
            waterInBody == null &&
            weight == null
}