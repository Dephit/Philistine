package com.sergeenko.alexey.noble.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sergeenko.alexey.noble.dataclasses.Client

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

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY sirname ASC")
    fun getClientsOrderBySirnameAsc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' ORDER BY sirname ASC")
    fun getClientsOrderBySirnameAsc(name: String = "", surname: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' AND patronymic LIKE '%' || :patro || '%' ORDER BY sirname ASC")
    fun getClientsOrderBySirnameAsc(name: String = "", surname: String = "", patro: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%'ORDER BY sirname DESC")
    fun getClientsOrderBySirnameDesc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' ORDER BY sirname DESC")
    fun getClientsOrderBySirnameDesc(name: String = "", surname: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' AND patronymic LIKE '%' || :patro || '%' ORDER BY sirname DESC")
    fun getClientsOrderBySirnameDesc(name: String = "", surname: String = "", patro: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY lastVisit ASC")
    fun getClientsOrderByDateAsc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' ORDER BY lastVisit ASC")
    fun getClientsOrderByDateAsc(name: String = "", surname: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' AND patronymic LIKE '%' || :patro || '%' ORDER BY lastVisit ASC")
    fun getClientsOrderByDateAsc(name: String = "", surname: String = "", patro: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' OR page_name LIKE '%' || :name || '%' OR patronymic LIKE '%' || :name || '%' ORDER BY lastVisit DESC")
    fun getClientsOrderByDateDesc(name: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' ORDER BY lastVisit DESC")
    fun getClientsOrderByDateDesc(name: String = "", surname: String = ""): List<Client>

    @Query("SELECT * FROM Client WHERE sirname LIKE '%' || :name || '%' AND page_name LIKE '%' || :surname || '%' OR sirname LIKE '%' || :surname || '%' AND page_name LIKE '%' || :name || '%' AND patronymic LIKE '%' || :patro || '%' ORDER BY lastVisit DESC")
    fun getClientsOrderByDateDesc(name: String = "", surname: String = "", patro: String = ""): List<Client>

}