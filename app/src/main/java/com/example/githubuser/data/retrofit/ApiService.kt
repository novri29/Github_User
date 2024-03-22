package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.GithubUserResponse
import com.example.githubuser.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization:token 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")
    fun getGitHubSearchUser(
        @Query("q") query: String
    ) : Call<GithubUserResponse>

    @GET("/users/{username}")
    @Headers("Authorization:token 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")
    fun getUsername(
    @Path("username") username : String
    ) : Call <DetailUserResponse>

    @GET("/users/{username}/followers")
    @Headers("Authorization:token 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")
    fun getFollowers (
    @Path("username") username: String
    ) : Call <List<ItemsItem>>

    @GET("/users/{username}/following")
    @Headers("Authorization:token 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")
    fun getFollowing (
    @Path("username") username: String
    ) : Call <List<ItemsItem>>
}