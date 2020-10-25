package com.sergeenko.alexey.noble.dataclasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.sergeenko.alexey.noble.getAgeFromLong
import com.sergeenko.alexey.noble.stringToByteArray
import com.sergeenko.alexey.noble.toByteArray
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
        var foto: String? = null,
        var height: String? = null,
        var hit: String? = null,
        var measurements: String? = null,
        @TypeConverters(MeasureListConvert::class)
        var measurementsList: MutableList<Measure>? = null,
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
    fun getClientAge(lang: Language) = age?.toLong()?.let { "${getAgeFromLong(it)} ${lang.years}" } ?: ""

    @Ignore
    fun getClientLastVisit() = dateS

    @Ignore
    fun getClientAddFormData(): MutableList<MultipartBody.Part> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
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

}


@Dao
interface ClientDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClient(client: Client)

    @Update
    fun updateClient(client: Client)

    @Delete
    fun deleteClient(client: Client)

    @Query("SELECT * FROM Client")
    fun getClientsLiveData(): LiveData<List<Client>>

    @Query("SELECT * FROM Client WHERE id LIKE :id")
    fun getClientById(id: String): Client

    /*@Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%'")
    fun getClients(name: String = ""): List<Client>*/

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY sirname  ASC")
    fun getClientsOrderBySirnameAsc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY sirname  DESC")
    fun getClientsOrderBySirnameDesc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY dateS  ASC")
    fun getClientsOrderByDateAsc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY dateS  DESC")
    fun getClientsOrderByDateDesc(name: String = ""): List<Client>
}

class BitmapConvector {
    @TypeConverter
    fun fromString(value: String?): Bitmap? {
        val ba = value?.stringToByteArray()
        return  try {
            ba?.size?.let { BitmapFactory.decodeByteArray(ba, 0, it) }
        } catch (e: Exception){
            null
        }
    }

    @TypeConverter
    fun toString(bitmap: Bitmap?): String? {
        return bitmap?.toByteArray().contentToString()
    }
}