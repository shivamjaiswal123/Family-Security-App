package com.example.myfamily

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactModel: ContactModel)

    //suspend function execute on background thread , suspend function will never execute on UI Thread
    //it can be called from coroutine or another suspend function
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactModelList: List<ContactModel>)     //used for inserting all contacts in Room DB

    @Query("SELECT * FROM contactmodel")
    suspend fun getAllContacts(): List<ContactModel>
}