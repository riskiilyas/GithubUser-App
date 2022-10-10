package com.keecoding.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.keecoding.githubuser.data.local.model.UserDetailEntity

@Database(entities = [UserDetailEntity::class], version = 1, exportSchema = false)
abstract class UserDb : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDb? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDb {
            if (INSTANCE == null) {
                synchronized(UserDb::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDb::class.java, "user_db"
                    )
                        .build()
                }
            }
            return INSTANCE as UserDb
        }
    }
}