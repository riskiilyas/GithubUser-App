package com.keecoding.githubuser

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keecoding.githubuser.utils.ViewModelFactory

abstract class BaseActivity : AppCompatActivity() {

    fun <V : ViewModel> getViewModel(type: Class<V>): V {
        val factory = ViewModelFactory.getInstance(application)
        return ViewModelProvider(this, factory)[type]
    }

}