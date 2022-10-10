package com.keecoding.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.keecoding.girhubusersub2.databinding.FragmentFollowersBinding
import com.keecoding.githubuser.BaseFragment
import com.keecoding.githubuser.adapter.UserAdapter
import com.keecoding.githubuser.viewmodel.UserViewModel
import com.keecoding.githubuser.data.local.model.User
import com.keecoding.githubuser.utils.Resource

class FollowersFragment : BaseFragment() {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserViewModel
    private var username: String? = null
    private var users: List<User>? = null
    private var hasInitilized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(UserViewModel::class.java)
        if (username == null) {
            binding.tvNoInternet.visibility = View.VISIBLE
        } else {
            viewModel.getFollowers(username!!)
            binding.tvNoInternet.visibility = View.INVISIBLE
        }

        val userAdapter = UserAdapter {
            hasInitilized = true
            viewModel.getUserDetail(it.username)
        }

        viewModel.followers.observe(viewLifecycleOwner) {
            if (hasInitilized && username != null) return@observe
            when (it) {
                is Resource.LOADING -> {
                }

                is Resource.SUCCESS -> {
                    users = it.result
                    userAdapter.changeList(it.result)
                }

                is Resource.ERROR -> {
                    showToast("Error Loading Followers!")
                }

                else -> {
                }
            }
        }

        binding.rvFollowers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun setupArguments(arguments: Bundle?) {
        arguments?.let {
            username = it.getString("username")
        }
    }
}