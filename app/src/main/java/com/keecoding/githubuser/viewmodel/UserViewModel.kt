package com.keecoding.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keecoding.githubuser.data.local.model.User
import com.keecoding.githubuser.data.local.model.UserDetailEntity
import com.keecoding.githubuser.repository.UserRepository
import com.keecoding.githubuser.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(app: Application) : ViewModel() {

    private var _currentUser = MutableLiveData<Resource<UserDetailEntity>>(Resource.INIT())
    val currentUser get() = _currentUser as LiveData<Resource<UserDetailEntity>>

    private val _listUser = MutableLiveData<Resource<List<User>>>(Resource.INIT())
    val listUser get() = _listUser as LiveData<Resource<List<User>>>

    private val _followings = MutableLiveData<Resource<List<User>>>(Resource.INIT())
    val followings get() = _followings as LiveData<Resource<List<User>>>

    private val _followers = MutableLiveData<Resource<List<User>>>(Resource.INIT())
    val followers get() = _followers as LiveData<Resource<List<User>>>

    private val _searchedUsers = MutableLiveData<Resource<List<User>>>(Resource.INIT())
    val searchedUsers get() = _searchedUsers as LiveData<Resource<List<User>>>

    private val _favoriteUsers = MutableLiveData<List<UserDetailEntity>>(emptyList())
    val favoriteUsers get() = _favoriteUsers as LiveData<List<UserDetailEntity>>

    private val repository = UserRepository(app)

    init {
        getUsers()
        getFavorites()
    }

    // DATABASE REQUESTS

    fun getFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getFavoriteUsers().collect {
                _favoriteUsers.postValue(it)
            }
        }
    }

    fun addFavorite(user: UserDetailEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.addFavorite(user)
        }
    }

    fun removeFavorite(user: UserDetailEntity) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteFavorite(user)
        }
    }

    fun deleteAllFavorites() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.deleteAllFavorites()
        }
    }

    // REMOTE REQUESTS

    fun getUsers() {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getUsers().collect {
                _listUser.postValue(it)
            }
        }
    }

    fun getUserDetail(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getUserDetail(username).collect {
                _currentUser.postValue(it)
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getFollowing(username).collect {
                _followings.postValue(it)
            }
        }
    }

    fun getFollowers(username: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getFollowers(username).collect {
                _followers.postValue(it)
            }
        }
    }

    fun searchUsers(query: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repository.searchUser(query).collect {
                _searchedUsers.postValue(it)
            }
        }
    }

    fun neutralizeCurrentUser() {
        _currentUser.postValue(Resource.INIT())
    }

    fun closeSearchUsers() {
        _searchedUsers.postValue(Resource.INIT())
        _listUser.postValue(_listUser.value)
    }

    fun isInFavorites(user: UserDetailEntity): Boolean {
        return if (favoriteUsers.value == null) false else {
            favoriteUsers.value?.forEach {
                if (it.username == user.username) return true
            }
            false
        }
    }

}