package com.keecoding.githubuser.ui

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.keecoding.girhubusersub2.R
import com.keecoding.girhubusersub2.databinding.FragmentHomeBinding
import com.keecoding.githubuser.BaseFragment
import com.keecoding.githubuser.adapter.UserAdapter
import com.keecoding.githubuser.ui.dialog.SettingsDialog
import com.keecoding.githubuser.viewmodel.UserViewModel
import com.keecoding.githubuser.utils.Resource
import com.keecoding.githubuser.viewmodel.PrefViewModel
import kotlinx.coroutines.*

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UserViewModel
    private lateinit var prefViewModel: PrefViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(UserViewModel::class.java)
        prefViewModel = getViewModel(PrefViewModel::class.java)
        viewModel.getUsers()

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView

        binding.toolbar.menu.apply {
            searchView = findItem(R.id.menuSearch).actionView as SearchView

            findItem(R.id.menuFav).setOnMenuItemClickListener {
                navigate(HomeFragmentDirections.actionHomeFragmentToFavoriteFragment())
                true
            }

            findItem(R.id.menuSettings).setOnMenuItemClickListener {
                SettingsDialog().show(requireActivity().supportFragmentManager, "Settings")
                true
            }

        }

        val adapter = UserAdapter {
            searchView.setQuery("", false)
            searchView.clearFocus()
            val toDetailFragment = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            toDetailFragment.username = it.username
            navigate(toDetailFragment)
        }

        viewModel.listUser.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.LOADING -> {
                    binding.swipeRefresh.isRefreshing = true
                }

                is Resource.SUCCESS -> {
                    binding.swipeRefresh.isRefreshing = false
                    adapter.changeList(it.result)
                }

                is Resource.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                    showToast("Error: ${it.msg}")
                }

                else -> {
                    binding.swipeRefresh.isRefreshing = false
                }
            }
        }

        viewModel.searchedUsers.observe(viewLifecycleOwner) {
            if (it is Resource.SUCCESS) {
                adapter.changeList(it.result)
            } else if (it is Resource.INIT) {
                adapter.clearData()
            }
            binding.swipeRefresh.isRefreshing = false
        }

        binding.rvUsers.apply {
            setAdapter(adapter)
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getUsers()
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = "Enter username here..."
        searchView.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchView.setBackgroundColor(Color.WHITE)

        var jobSearch: Job? = null

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(txt: String?): Boolean {
                jobSearch?.cancel()
                txt?.let {
                    jobSearch = lifecycleScope.launch(Dispatchers.Main) {
                        delay(DELAY_INPUT)
                        viewModel.searchUsers(it.trim())
                    }
                }
                binding.swipeRefresh.isRefreshing = true
                return true
            }

            override fun onQueryTextChange(txt: String?): Boolean {
                jobSearch?.cancel()
                txt?.let {
                    jobSearch = lifecycleScope.launch(Dispatchers.Main) {
                        delay(DELAY_INPUT)
                        viewModel.searchUsers(it.trim())
                    }
                }
                binding.swipeRefresh.isRefreshing = true
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.closeSearchUsers()
            true
        }


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure Exit?")
                        .setPositiveButton("Yes") { _, _ -> activity?.finish() }
                        .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            })
    }

    override fun setupArguments(arguments: Bundle?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val DELAY_INPUT = 300L
    }
}