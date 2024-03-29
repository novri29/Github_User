package com.example.githubuser.model

import android.app.Application
import android.content.ClipData.Item
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.database.Favorite
import com.example.githubuser.repo.FavoriteRepo
import com.example.githubuser.ui.DetailUserActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "DetailViewModel"
    }
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val favoriteRepo : FavoriteRepo = FavoriteRepo(application)
    fun detailUserGithub(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsername(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }


        })

    }
    fun insert(favorite: Favorite) {
        favoriteRepo.insert(favorite)
    }
    fun checkFavorite(login: String) : LiveData<Boolean> = favoriteRepo.checkFavorite(login)

    fun delete(favorite: Favorite) {
        favoriteRepo.delete(favorite)
    }
}

