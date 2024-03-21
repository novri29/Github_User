package com.example.githubuser.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.GithubUserResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.databinding.ActivityMainBinding
import com.google.android.material.search.SearchBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_ID ="hello"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvData.layoutManager = layoutManager

        binding.rvData.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this,layoutManager.orientation)
        binding.rvData.addItemDecoration(itemDecoration)

        findUser()

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
            viewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }


    private fun searchUser(found: Boolean) {
        binding.apply {
            if (found) {
                rvData.visibility = View.VISIBLE
            }else {
                rvData.visibility = View.GONE
            }
        }
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
            binding.progressBar.visibility = View.INVISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }

    }
}