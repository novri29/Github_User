package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.model.DetailViewModel
import com.example.githubuser.databinding.ActivityMainBinding

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        val detailUser = intent.getStringExtra(EXTRA_USER)
        if (detailUser != null){
            viewModel.detailUserGithub(detailUser)

        }
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        viewModel.detailUser.observe(this) { item ->
            setDetailUser(item)
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

    private fun setDetailUser(item: DetailUserResponse){
        binding.apply {
            tvNameuser.text = item.name
            tvUsername.text = item.login
            tvBio.text = item.bio.toString()
            tvFollowers.text = item.followers.toString()
            tvFollowing.text = item.following.toString()
            Glide.with(this@DetailUserActivity)
                .load(item.avatarUrl)
                .into(ivImage)
        }
    }

}