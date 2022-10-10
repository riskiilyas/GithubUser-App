package com.keecoding.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keecoding.girhubusersub2.databinding.FavoriteLayoutBinding
import com.keecoding.githubuser.data.local.model.UserDetailEntity

class FavoriteAdapter(
    private val list: MutableList<UserDetailEntity> = mutableListOf(),
    val onItemClicked: (UserDetailEntity) -> Unit,
    val onItemDeleted: (UserDetailEntity) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: FavoriteLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserDetailEntity) {
            binding.apply {
                Glide.with(root.context).load(user.avatarUrl).into(ivProfile)
                tvName.text = user.username
                tvLink.text = user.profileUrl

                btnDelete.setOnClickListener {
                    onItemDeleted(user)
                }

                root.setOnClickListener {
                    onItemClicked(user)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            FavoriteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun changeList(newList: List<UserDetailEntity>) {
        list.clear()
        newList.forEach {
            list.add(it)
        }
        notifyDataSetChanged()
    }

}