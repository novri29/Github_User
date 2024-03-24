package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.model.DetailViewModel
import com.example.githubuser.spa.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailViewModel


    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.Followers, R.string.Following)
        const val EXTRA_USER = "EXTRA_USER"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                // KLIK
                val intent = Intent(this, profileActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

}