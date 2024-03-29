package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite : Favorite)

    @Query("SELECT * FROM favorite")
    fun getFavorite(): LiveData<List<Favorite>>

    @Delete
    fun delete(favorite : Favorite)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE login = :login)")
    fun checkFavorite(login : String): LiveData<Boolean>
}