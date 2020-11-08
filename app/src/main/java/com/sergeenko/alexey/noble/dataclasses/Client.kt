package com.sergeenko.alexey.noble.dataclasses

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.sergeenko.alexey.noble.utills.convertLongToTimeDDMM
import com.sergeenko.alexey.noble.converters.BitmapConvector
import com.sergeenko.alexey.noble.converters.MeasureListConvert
import com.sergeenko.alexey.noble.converters.TrainingListConvert
import com.sergeenko.alexey.noble.utills.getAgeFromLong
import com.sergeenko.alexey.noble.utills.toByteArray
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.Serializable
import java.util.*

val MEDIA_TYPE_PNG = okhttp3.MediaType.parse("image/*")

@Entity
data class Client(
        @PrimaryKey(autoGenerate = false)
        var id: String = "-1",
        var age: Long? = null,
        var citate: String? = null,
        var club: String? = null,
        @SerializedName("dateS")
        var dateS: String? = null,
        var lastVisit: String? = null,
        var foto: String? = null,
        var height: String? = null,
        var hit: String? = null,
        var measurements: String? = null,
        @TypeConverters(MeasureListConvert::class)
        var measurementsList: MutableList<Measure>? = null,
        @TypeConverters(TrainingListConvert::class)
        var trainingList: MutableList<TrainingItem>? = null,
        var page_name: String? = null,
        var patronymic: String? = null,
        var phone: String? = null,
        var pos: String? = null,
        var pos5: String? = null,
        var sex: String? = null,
        var sirname: String? = null,
        var trainings: String? = null,
        var url: String? = null,
        var vis: String? = null,
        @TypeConverters(MeasureConvert::class)
        var lastMeasure: Measure? = null,
        var weight: String? = null,
        var needToBeAdded: Boolean = false,
        var needToBeEdited: Boolean = false,
        @TypeConverters(BitmapConvector::class)
        var bitmap: Bitmap? = null
): Serializable {
    @Ignore
    fun getName() = "${sirname ?: ""} ${page_name ?: ""}\n${patronymic ?: ""}"

    @Ignore
    fun getClientAge(lang: Language) = age?.let { "${getAgeFromLong(it)} ${lang.years}" } ?: ""

    @Ignore
    fun getClientLastVisit(lang: Language) = lastVisit?.let {
        "${if(sex == "woman") lang.she_was else lang.he_was}: ${convertLongToTimeDDMM(it.toLong())}"}

    private fun lastVisit() = trainingList?.lastOrNull()?.dateOfTraining

    @Ignore
    fun getClientAddFormData(): MutableList<MultipartBody.Part> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        if(id.toInt() > 0) {
            builder.addFormDataPart("client_id", id)
        }
        page_name?.let { builder.addFormDataPart("client_name", it) }
        sirname?.let { builder.addFormDataPart("client_sirname", it) }
        patronymic?.let { builder.addFormDataPart("client_patronymic", it) }
        sex?.let { builder.addFormDataPart("client_sex", it) }
        age?.let { builder.addFormDataPart("client_age", it.toString()) }
        vis?.let { builder.addFormDataPart("client_visit", it) }
        phone?.let { builder.addFormDataPart("client_phone", it) }
        height?.let { builder.addFormDataPart("client_height", it) }
        weight?.let { builder.addFormDataPart("client_weight", it) }
        measurements?.let { builder.addFormDataPart("client_measurements", it) }
        trainings?.let { builder.addFormDataPart("client_trainings", it) }
        bitmap?.let { builder.addFormDataPart("client_foto", "image.jpg", RequestBody.create(MEDIA_TYPE_PNG, it.toByteArray())) }
        return builder.build()
                .parts()

    }
    @Ignore
    fun addNewMeasure(dateOfMeasure: Long? = null, measure: Measure) {
        measure.dateOfMeasure = dateOfMeasure ?: Calendar.getInstance().time.time
        lastMeasure = measure
        measurementsList?.add(measure)
        measurements = JSONArray(measurements ?: "[]")
                .put(MeasureConvert()
                        .toJson(measure))
                .toString()
    }

    fun measuresFromJson() {
        measurementsList = MeasureListConvert().stringToSomeObjectList(measurements)
    }

    fun trainings() {
        trainingList = TrainingListConvert().stringObjectList(trainings)
        lastVisit = lastVisit()?.toString()
    }

    fun getLastSession() {
        try {
            val a = trainings?.substring(trainings!!.lastIndexOf("{\"muscles\""), trainings!!.lastIndexOf("]"))
            lastVisit = Gson().fromJson(a, TrainingItem::class.java).dateOfTraining.toString()
        }catch (e: Exception){

        }
    }

}


