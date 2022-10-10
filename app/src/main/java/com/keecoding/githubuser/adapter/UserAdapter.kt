package com.keecoding.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keecoding.girhubusersub2.databinding.UserLayoutBinding
import com.keecoding.githubuser.data.local.model.User

class UserAdapter(
    private val list: MutableList<User> = mutableListOf(),
    val onItemClicked: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                Glide.with(root.context).load(user.avatarUrl).into(ivProfile)
                tvName.text = user.username
                tvLink.text = user.profileUrl
                root.setOnClickListener {
                    onItemClicked(user)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun changeList(newList: List<User>) {
        list.clear()
        newList.forEach {
            list.add(it)
        }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }
}