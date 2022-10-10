package com.keecoding.githubuser.data.local.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = UserDetailEntity.TABLE_NAME)
@Parcelize
data class UserDetailEntity(
    @ColumnInfo(name = "USERNAME")
    @PrimaryKey
    val username: String,
    val name: String,
    val avatarUrl: String,
    val company: String,
    val bio: String,
    val profileUrl: String,
    val followers: Int,
    val following: Int,
    val location: String,
    val followersUrl: String,
    val followingUrl: String,
    val repo: Int
) : Parcelable {
    companion object {
        const val TABLE_NAME = "user"
    }
}