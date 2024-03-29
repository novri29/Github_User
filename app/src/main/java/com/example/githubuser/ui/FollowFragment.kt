package com.example.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.model.FollowViewModel


class FollowFragment : Fragment() {

    private lateinit var _binding : FragmentFollowBinding
    private val viewModel by viewModels<FollowViewModel>()

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        _binding.rvFollow.layoutManager = layoutManager
        _binding.rvFollow.addItemDecoration(itemDecoration)

        val number = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME) ?: ""

        if (number == 1) {
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            viewModel.getFollowersList(username)
            viewModel.followersList.observe(viewLifecycleOwner) {
                    followers -> recyclerViewSetUp(followers)
            }

        } else {
            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                showLoading(isLoading)
            }
            viewModel.getFollowingList(username)
            viewModel.followingList.observe(viewLifecycleOwner) {
                    following -> recyclerViewSetUp(following)
            }
        }
    }

    private fun recyclerViewSetUp(followers: List<ItemsItem>?) {
        val adapter = UserAdapter()
        followers?.let {
            adapter.submitList(it)
        }
        _binding.rvFollow.adapter = adapter
        showLoading(false)
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            _binding.progressBar.visibility = View.VISIBLE
        } else {
            _binding.progressBar.visibility = View.GONE
        }

    }

}
