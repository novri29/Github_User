package com.example.githubuser.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.model.DetailViewModel
import com.example.githubuser.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var favoriteUser: Favorite



    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.Followers, R.string.Following)
        const val EXTRA_USER = "EXTRA_USER"
        private val SAVE_FAV = R.string.save_favorite
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val detailUser = intent.getStringExtra("EXTRA_USER")
        //ViewPager
        val sectionPagerAdapter = detailUser?.let {SectionPagerAdapter(this, it)}
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs,viewPager) {
                tab, position -> tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        if (detailUser != null){
            viewModel.detailUserGithub(detailUser)

        }

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

    //DetailUser
    private fun setDetailUser(item: DetailUserResponse){
        binding.apply {
            tvNameuser.text = item.name
            tvUsername.text = item.login
            tvBio.text = item.bio.toString()
            tvFollowers.text = getString(R.string.Followers) + " \n " + item.followers.toString()
            tvFollowing.text = getString(R.string.Following) + " \n " + item.following.toString()
            Glide.with(this@DetailUserActivity)
                .load(item.avatarUrl)
                .circleCrop()
                .into(ivImage)
        }
        favoriteUser = Favorite(item.login ?: "", item.avatarUrl ?: "")
        viewModel.checkFavorite(favoriteUser.login ?: "").observe(this) { fav1 ->
            setFavorite(fav1)
        }
        binding.ivFavorite.apply {
            setOnClickListener {
                if (tag == SAVE_FAV) {
                    viewModel.delete(favoriteUser)
                } else {
                    viewModel.insert(favoriteUser)
                }
            }
        }
    }

    private fun setFavorite(fav: Boolean) {
        binding.ivFavorite.apply {
            if(fav) {
                tag = SAVE_FAV
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailUserActivity,R.drawable.baseline_favorite_24
                    )
                )
            } else {
                tag = ""
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailUserActivity,R.drawable.baseline_favorite_border_24
                    )
                )
            }
        }
    }

}