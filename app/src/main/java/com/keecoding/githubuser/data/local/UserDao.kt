package com.keecoding.githubuser.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keecoding.githubuser.data.local.model.UserDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(userDetailEntity: UserDetailEntity)

    @Query("SELECT * FROM ${UserDetailEntity.TABLE_NAME}")
    fun getFavorites(): Flow<List<UserDetailEntity>>

    @Delete(entity = UserDetailEntity::class)
    suspend fun deleteUser(user: UserDetailEntity)

    @Query("DELETE FROM ${UserDetailEntity.TABLE_NAME}")
    suspend fun deleteAll()

}