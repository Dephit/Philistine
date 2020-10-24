package com.sergeenko.alexey.noble.dataclasses

data class Measures(
    var hipsVolume: Int? = null,
    var chestVolume: Int? = null,
    var leftHandVolume: Int? = null,
    var leftHipVolume: Int? = null,
    var rightHandVolume: Int? = null,
    var rightHipVolume: Int? = null,
    var waistVolume: Int? = null
){
    fun isEmpty() = hipsVolume == null &&
                    chestVolume == null &&
                    leftHandVolume == null &&
                    leftHipVolume == null &&
                    rightHandVolume == null &&
                    rightHipVolume == null &&
                    waistVolume == null

}