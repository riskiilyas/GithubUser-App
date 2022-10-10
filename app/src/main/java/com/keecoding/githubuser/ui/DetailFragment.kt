package com.keecoding.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.keecoding.girhubusersub2.R
import com.keecoding.girhubusersub2.databinding.FragmentDetailBinding
import com.keecoding.githubuser.BaseFragment
import com.keecoding.githubuser.adapter.FollowPagerAdapter
import com.keecoding.githubuser.viewmodel.UserViewModel
import com.keecoding.githubuser.data.local.model.UserDetailEntity
import com.keecoding.githubuser.utils.Resource


class DetailFragment : BaseFragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserViewModel
    private var userDetail: UserDetailEntity? = null
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(UserViewModel::class.java)

        if (userDetail != null) {
            userDetail?.let { setupView() }
        }

        binding.apply {
            viewModel.currentUser.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.LOADING -> {
                        swipeRefresh.isRefreshing = true
                    }

                    is Resource.SUCCESS -> {
                        if (userDetail != null && userDetail?.username != it.result.username) {
                            val toDetailFragment =
                                DetailFragmentDirections.actionDetailFragmentSelf()

                            toDetailFragment.username = it.result.username
                            navigate(toDetailFragment)
                        } else if (userDetail == null && username == it.result.username) {
                            userDetail = it.result
                            setupView()

                            if (viewModel.isInFavorites(it.result)) {
                                binding.fabFavorite.visibility = View.INVISIBLE
                            }
                        }
                        swipeRefresh.isRefreshing = false
                    }

                    is Resource.ERROR -> {
                        swipeRefresh.isRefreshing = false
                        showToast(it.msg)
                    }

                    else -> {
                        if (userDetail != null) return@observe
                        detailLayout.visibility = View.INVISIBLE
                        swipeRefresh.isRefreshing = false
                    }
                }
            }

            val pagerAdapter = FollowPagerAdapter(baseActivity, username)

            val viewPager = viewPager.apply {
                adapter = pagerAdapter
            }

            TabLayoutMediator(tabs, viewPager) { tab, pos ->
                tab.text = resources.getStringArray(R.array.tabs)[pos]
            }.attach()

            fabFavorite.setColorFilter(Color.WHITE)

            swipeRefresh.setOnRefreshListener {
                viewModel.getUserDetail(username)
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    viewModel.neutralizeCurrentUser()
                    popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.getUserDetail(username)
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        userDetail?.let { user ->
            binding.apply {
                Glide.with(requireContext()).load(user.avatarUrl).into(binding.ivProfile)
                tvName.text = user.name
                tvUsername.text = user.username
                tvCompany.text = user.company
                tvLocation.text = user.location
                tvFollowers.text = "${user.followers}\nFollowers"
                tvFollowing.text = "${user.following}\nFollowing"
                tvRepo.text = "${user.repo}\nRepositories"

                toolbar.title = user.name

                detailLayout.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false

                binding.fabFavorite.setOnClickListener {
                    userDetail?.let { mUser ->
                        showToast("Added To Favorite!")
                        viewModel.addFavorite(mUser)
                        binding.fabFavorite.visibility = View.GONE
                    }
                }

                toolbar.menu.findItem(R.id.menuShare).setOnMenuItemClickListener {
                    shareProfile(user.profileUrl)
                    true
                }

                toolbar.menu.findItem(R.id.menuback).setOnMenuItemClickListener {
                    viewModel.neutralizeCurrentUser()
                    popBackStack()
                    true
                }
            }
        }
    }

    private fun shareProfile(url: String) {
        val mIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/uri-list"
        }
        startActivity(mIntent)
    }

    override fun setupArguments(arguments: Bundle?) {
        arguments?.let {
            username = it.getString("username", "")
            (it.getParcelable("saved_entity") as UserDetailEntity?)?.apply {
                userDetail = this
                binding.fabFavorite.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}