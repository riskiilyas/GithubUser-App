package com.keecoding.githubuser.adapter

import android.os.Bundle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.keecoding.githubuser.BaseActivity
import com.keecoding.githubuser.BaseFragment
import com.keecoding.githubuser.ui.FollowersFragment
import com.keecoding.githubuser.ui.FollowingFragment

class FollowPagerAdapter(
    activity: BaseActivity,
    private val username: String
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): BaseFragment {
        var fragment: BaseFragment? = null

        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }

        fragment?.arguments = Bundle().apply { putString("username", username) }

        return fragment!!
    }
}