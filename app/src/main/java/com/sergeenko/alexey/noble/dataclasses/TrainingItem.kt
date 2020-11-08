package com.sergeenko.alexey.noble.dataclasses

import androidx.room.Entity
import androidx.room.TypeConverters
import com.sergeenko.alexey.noble.converters.MuscleListTypeConveter
import java.io.Serializable

@Entity(primaryKeys = ["id", "clientId"])
data class TrainingItem(
        var id: Int = 0,
        var clientId: Int = 0,
        var commonPower: Int = 0,
        var dateOfTraining: Long = 0,
        var frequency: Int = 0,
        var frequencyFill: Int = 0,
        var impulse: Int = 0,
        var isTrainingFull: Boolean = false,
        @TypeConverters(MuscleListTypeConveter::class)
        var muscles: List<Muscle>? = null,
        var name: String = "",
        var pause: Int = 0,
        var totalMinutes: Int = 0,
        var totalSeconds: Int = 0
): Serializable

