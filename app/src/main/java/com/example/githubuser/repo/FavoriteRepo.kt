package com.example.githubuser.repo

import android.app.Application
import com.example.githubuser.database.Favorite
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepo(application: Application) {
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val favoriteDao : FavoriteDao

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }

    fun getFavorite() = favoriteDao.getFavorite()
    fun insert(favorite: Favorite) {
        executorService.execute { favoriteDao.insert(favorite)}
    }
    fun checkFavorite(login : String) = favoriteDao.checkFavorite(login)

    fun delete(favorite: Favorite) {
        executorService.execute { favoriteDao.delete(favorite)}
    }
}