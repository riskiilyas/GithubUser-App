package com.keecoding.githubuser.repository

import android.app.Application
import com.keecoding.githubuser.data.local.UserDb
import com.keecoding.githubuser.data.local.model.User
import com.keecoding.githubuser.data.local.model.UserDetailEntity
import com.keecoding.githubuser.data.remote.ApiConfig
import com.keecoding.githubuser.data.remote.response.UserResponse
import com.keecoding.githubuser.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import retrofit2.awaitResponse

class UserRepository(
    app: Application
) {

    private val db = UserDb.getDatabase(app)
    private val dao = db.userDao()
    private val service = ApiConfig.getApiService()

    // LOCAL SEGMENT //

    suspend fun addFavorite(user: UserDetailEntity) {
        dao.addFavorite(user)
    }

    fun getFavoriteUsers() = dao.getFavorites()

    suspend fun deleteFavorite(user: UserDetailEntity) {
        dao.deleteUser(user)
    }

    suspend fun deleteAllFavorites() {
        dao.deleteAll()
    }

    // NETWORK SEGMENT //

    suspend fun getUsers(): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.LOADING())
            try {
                val response = service.getUsers().awaitResponse()
                emitResult(response)

            } catch (e: Exception) {
                emit(Resource.ERROR(e.message ?: "Unknown Error"))
            }
        }
    }

    suspend fun getUserDetail(username: String): Flow<Resource<UserDetailEntity>> {
        return flow {
            emit(Resource.LOADING())
            try {
                val response = service.getUser(username).awaitResponse()
                if (response.isSuccessful && response.body() != null) {
                    emit(Resource.SUCCESS(response.body()!!.toUserDetail()))
                } else {
                    emit(Resource.ERROR("Unknown Error!"))
                }
            } catch (e: Exception) {
                emit(Resource.ERROR(e.message ?: "Unknown Error"))
            }
        }
    }

    suspend fun getFollowing(username: String): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.LOADING())
            try {
                val response = service.getFollowing(username).awaitResponse()
                emitResult(response)

            } catch (e: Exception) {
                emit(Resource.ERROR(e.message ?: "Unknown Error"))
            }
        }
    }

    suspend fun getFollowers(username: String): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.LOADING())
            try {
                val response = service.getFollowers(username).awaitResponse()
                emitResult(response)

            } catch (e: Exception) {
                emit(Resource.ERROR(e.message ?: "Unknown Error"))
            }
        }
    }

    suspend fun searchUser(query: String): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.LOADING())
            try {
                val response = service.searchUsers(query).awaitResponse()
                if (response.isSuccessful && response.body() != null) {
                    val mUser = mutableListOf<User>()
                    response.body()!!.items.forEach { e ->
                        mUser.add(e.toUser())
                    }
                    emit(Resource.SUCCESS(mUser))
                } else {
                    emit(Resource.ERROR("Unknown Error!"))
                }

            } catch (e: Exception) {
                emit(Resource.ERROR(e.message ?: "Unknown Error"))
            }
        }
    }

    companion object {
        suspend fun FlowCollector<Resource<List<User>>>.emitResult(response: Response<List<UserResponse>>) {

            if (response.isSuccessful && response.body() != null) {
                val mUser = mutableListOf<User>()
                response.body()!!.forEach { e ->
                    mUser.add(e.toUser())
                }
                emit(Resource.SUCCESS(mUser))
            } else {
                emit(Resource.ERROR("Unknown Error!"))
            }
        }
    }
}