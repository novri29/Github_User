package com.example.githubuser.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.database.Favorite
import com.example.githubuser.repo.FavoriteRepo

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteRepo : FavoriteRepo = FavoriteRepo(application)

    fun getFavorite(): LiveData<List<Favorite>> = favoriteRepo.getFavorite()
    init {
        getFavorite()
    }
}