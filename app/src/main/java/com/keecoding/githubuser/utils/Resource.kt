package com.keecoding.githubuser.utils

sealed class Resource<R> {
    class INIT<R> : Resource<R>()
    class LOADING<R> : Resource<R>()
    data class ERROR<R>(val msg: String) : Resource<R>()
    data class SUCCESS<R>(val result: R) : Resource<R>()
}