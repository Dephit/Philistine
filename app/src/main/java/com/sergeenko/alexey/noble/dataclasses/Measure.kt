package com.sergeenko.alexey.noble.dataclasses

data class Measure(
    var dateOfMeasure: Long? = null,
    var measures: Measures = Measures(),
    var params: Params = Params()
)