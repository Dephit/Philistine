package com.sergeenko.alexey.noble.dataclasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.room.*
import com.sergeenko.alexey.noble.*
import com.squareup.picasso.Picasso
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.util.*

val MEDIA_TYPE_PNG = okhttp3.MediaType.parse("image/*")

@Entity
data class Client(
    @PrimaryKey(autoGenerate = false)
    var id: String = "-1",
    val age: String? = null,
    val citate: String? = null,
    val club: String? = null,
    val dateS: String? = null,
    val foto: String? = null,
    val height: String? = null,
    val hit: String? = null,
    val measurements: String? = null,
    val page_name: String? = null,
    val patronymic: String? = null,
    val phone: String? = null,
    val pos: String? = null,
    val pos5: String? = null,
    val sex: String? = null,
    val sirname: String? = null,
    val trainings: String? = null,
    val url: String? = null,
    val vis: String? = null,
    val weight: String? = null,
    val needToBeAdded: Boolean = false,
    val needToBeEdited: Boolean = false,
    @TypeConverters(BitmapConvector::class)
    var bitmap: Bitmap? = null
){
    @Ignore
    fun getName() = "${sirname ?: ""} ${page_name ?: ""}\n${patronymic ?: ""}"

    @Ignore
    fun getClientAge(lang: Language) = age?.toLong()?.let { "${getAgeFromLong(it)} ${lang.years}" } ?: ""

    @Ignore
    fun getClientLastVisit() = ""

    @Ignore
    fun getClientAddFormData(): MutableList<MultipartBody.Part> {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        page_name?.let { builder.addFormDataPart("client_name", it) }
        sirname?.let { builder.addFormDataPart("client_sirname", it) }
        patronymic?.let { builder.addFormDataPart("client_patronymic", it) }
        sex?.let { builder.addFormDataPart("client_sex", it) }
        age?.let { builder.addFormDataPart("client_age", it) }
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