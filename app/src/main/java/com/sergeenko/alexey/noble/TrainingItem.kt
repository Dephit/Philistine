package com.sergeenko.alexey.noble

import java.io.Serializable

data class TrainingItem(
    var commonPower: Int = 0,
    var dateOfTraining: Long = 0,
    var frequency: Int = 0,
    var frequencyFill: Int = 0,
    var impulse: Int = 0,
    var isTrainingFull: Boolean = false,
    var muscles: List<Muscle> = listOf(),
    var name: String = "",
    var pause: Int = 0,
    var totalMinutes: Int = 0,
    var totalSeconds: Int = 0
): Serializable