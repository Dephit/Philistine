package com.sergeenko.alexey.noble.dataclasses

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.room.*
import com.squareup.picasso.Picasso
import com.sergeenko.alexey.noble.R
import com.sergeenko.alexey.noble.getAgeFromLong
import org.json.JSONArray
import java.util.*

@Entity
data class Client(
    @PrimaryKey(autoGenerate = false)
    val id: String = "1",
    val age: String?,
    val citate: String?,
    val club: String?,
    val dateS: String?,
    val foto: String?,
    val height: String?,
    val hit: String?,
    val measurements: String?,
    val page_name: String?,
    val patronymic: String?,
    val phone: String?,
    val pos: String?,
    val pos5: String?,
    val sex: String?,
    val sirname: String?,
    val trainings: String?,
    val url: String?,
    val vis: String?,
    val weight: String
){
    @Ignore
    fun getName() = "$sirname ${page_name}\n${patronymic}"

    @Ignore
    fun getClientAge(lang: Language) = age?.toLong()?.let { "${getAgeFromLong(it)} ${lang.years}" } ?: ""

    @Ignore
    fun getClientLastVisit() = ""
}

@Dao
interface ClientDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertClient(client: Client)

    @Update
    fun updateClient(client: Client)

    @Delete
    fun deleteClient(client: Client)

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

