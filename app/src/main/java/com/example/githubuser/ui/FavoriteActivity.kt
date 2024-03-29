package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.AdapterFavorite
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.model.FavoriteViewModel
import com.example.githubuser.model.ViewModelFactory
import com.example.githubuser.ui.DetailUserActivity.Companion.EXTRA_USER

class FavoriteActivity : AppCompatActivity(), AdapterFavorite.OnClickListener {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter : AdapterFavorite
    private val viewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }
    @SuppressLint("SuspiciousIndentation")
    override fun clickItem(item: Favorite) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(EXTRA_USER, item.login)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager

        adapter = AdapterFavorite(this)
        binding.rvFavorite.adapter = adapter

        viewModel.getFavorite().observe(this) { items ->
            adapter.submitList(items.sortedBy { it.login })
        }

    }


}