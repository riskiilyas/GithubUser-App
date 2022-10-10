package com.keecoding.githubuser

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.keecoding.girhubusersub2.R
import com.keecoding.githubuser.viewmodel.PrefViewModel

class MainActivity : BaseActivity() {
    private lateinit var prefViewModel: PrefViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefViewModel = getViewModel(PrefViewModel::class.java)

        prefViewModel.darkMode.observe(this) {
            AppCompatDelegate.setDefaultNightMode(if (it) MODE_NIGHT_YES else MODE_NIGHT_NO)
        }

    }

}