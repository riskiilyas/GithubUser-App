package com.keecoding.githubuser.data.remote

import com.keecoding.githubuser.data.remote.response.SearchUsersResponse
import com.keecoding.githubuser.data.remote.response.UserDetailResponse
import com.keecoding.githubuser.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("users")
    fun getUsers(): Call<List<UserResponse>>

    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UserResponse>>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UserResponse>>

    @GET("search/users")
    fun searchUsers(@Query("q") q: String): Call<SearchUsersResponse>
}