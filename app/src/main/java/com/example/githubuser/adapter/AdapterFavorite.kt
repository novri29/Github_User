package com.example.githubuser.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.DetailUserActivity

class AdapterFavorite(private val onItemClick: OnClickListener?) :
    RecyclerView.Adapter<AdapterFavorite.FavoriteViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitList(list: List<Favorite>) {
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = differ.currentList.getOrNull(position) ?: return
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class FavoriteViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context: Context = v?.context ?: return
            val item = differ.currentList.getOrNull(adapterPosition) ?: return
            val intent = Intent(context, DetailUserActivity::class.java).apply {
                putExtra(DetailUserActivity.EXTRA_USER,item.login)
                putExtra("login", item.login)
                putExtra("avatarURL", item.avatarURL)
            }
            context.startActivity(intent)
        }

        fun bind(item: Favorite) {
            with(binding) {
                Glide.with(ivUserProfile)
                    .load(item.avatarURL)
                    .circleCrop()
                    .into(ivUserProfile)
                tvUsername.text = item.login
            }
        }
    }

    interface OnClickListener {
        fun clickItem(item: Favorite)
    }
}
