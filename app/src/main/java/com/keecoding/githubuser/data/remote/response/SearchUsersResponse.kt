package com.keecoding.githubuser.data.remote.response


import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val items: List<SearchedUserResponse>,
    @SerializedName("total_count")
    val totalCount: Int
)