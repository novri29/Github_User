package com.example.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.model.FollowViewModel


class FollowFragment : Fragment() {

    private lateinit var _binding : FragmentFollowBinding
    private val  viewModel by viewModels<FollowViewModel>()
    private var adapter = UserAdapter()

    companion object {
        const val ARG_SECTION_NUMBER = "SECTION_NUMBER"
        const val ARG_USERNAME = "USERNAME"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val number = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val user = arguments?.getString(ARG_USERNAME) ?: ""


        if (number == 1) {
            viewModel.getFollowersList(user)
            viewModel.followersList.observe(viewLifecycleOwner) {
                    followers -> recyclerViewSetUp(followers)
            }

        } else {
            viewModel.getFollowingList(user)
            viewModel.followingList.observe(viewLifecycleOwner) {
                    following -> recyclerViewSetUp(following)
            }
        }
        showLoading(isLoading = true)
    }

    private fun recyclerViewSetUp(followers: List<ItemsItem>?) {
        _binding.rvFollow.adapter = adapter
        val adapter = UserAdapter()
        followers?.let {
            adapter.submitList(it)
        }

    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            _binding.progressBar.visibility = View.VISIBLE
        } else {
            _binding.progressBar.visibility = View.GONE
        }

    }


}
