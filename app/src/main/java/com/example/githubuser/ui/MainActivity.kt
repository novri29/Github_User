package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.model.MainViewModel
import com.example.githubuser.data.response.GithubUserResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.model.SettingViewModel
import com.example.githubuser.model.SettingViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID ="Novri"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                // KLIK ke Menu Setting
                val intent = Intent(this, SettingActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvData.layoutManager = layoutManager

        binding.rvData.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvData.addItemDecoration(itemDecoration)

        findUser()

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.findUsersGithub(searchView.text.toString())
                    false
                }
            viewModel.isLoading.observe(this@MainActivity) { isLoading ->
                showLoading(isLoading)
            }
            viewModel.listUser.observe(this@MainActivity) { items ->
                setDataUser(items)
                searchUser(!items.isNullOrEmpty())
            }
        }
        val pref = SettingPreference.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive : Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
        /**
        with(binding) {
        searchView.setupWithSearchBar(searchBar)
        searchView
        .editText
        .setOnEditorActionListener { textView, actionId, event ->
        searchBar.setText(searchView.text)
        searchView.hide()
        viewModel.findUsersGithub(searchView.text.toString())
        viewModel.listUser.observe(this@MainActivity)  {
        if (it.isNullOrEmpty()) {
        searchUser(false)

        } else {
        searchUser(true)
        }
        }
        false
        }
        }
         **/

    private fun searchUser(found: Boolean) {
        binding.rvData.visibility = if (found) View.VISIBLE else View.GONE
    }


    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getGitHubSearchUser(USER_ID)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setDataUser(responseBody.items)
                        showLoading(false)
                    }
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
            }

        })
    }

    private fun setDataUser(items: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        binding.rvData.adapter = adapter
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }

}