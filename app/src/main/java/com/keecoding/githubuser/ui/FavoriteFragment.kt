package com.keecoding.githubuser.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.keecoding.girhubusersub2.R
import com.keecoding.girhubusersub2.databinding.FragmentFavoriteBinding
import com.keecoding.githubuser.BaseFragment
import com.keecoding.githubuser.adapter.FavoriteAdapter
import com.keecoding.githubuser.viewmodel.UserViewModel

class FavoriteFragment : BaseFragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding as FragmentFavoriteBinding

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(UserViewModel::class.java)

        val mAdapter = FavoriteAdapter(onItemClicked = {
            val toDetail = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
            toDetail.username = it.username
            toDetail.savedEntity = it
            navigate(toDetail)
        }) {
            viewModel.removeFavorite(it)
            showToast("${it.username} is deleted")
        }

        viewModel.favoriteUsers.observe(viewLifecycleOwner) {
            binding.pbLoading.visibility = View.GONE
            if (it.isNullOrEmpty()) binding.tvNoFavs.visibility = View.VISIBLE
            else binding.tvNoFavs.visibility = View.INVISIBLE

            mAdapter.changeList(it)
        }

        binding.rvFavs.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.materialToolbar.menu.apply {
            findItem(R.id.menuDeleteAll).setOnMenuItemClickListener {
                displayDeleteConfirmDialog()
                true
            }

            findItem(R.id.backFromFav).setOnMenuItemClickListener {
                popBackStack()
                true
            }
        }

        viewModel.getFavorites()
    }

    private fun displayDeleteConfirmDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Are you sure Delete All Favorite?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteAllFavorites()
                showToast("All Favorites are cleared.")
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun setupArguments(arguments: Bundle?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}