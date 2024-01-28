package com.example.myshopnetapp.Activities.ui.Order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopnetapp.Adapters.UserOrdersAdapter
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.databinding.FragmentUserordersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UserOrderFragment : Fragment() {
    private lateinit var binding: FragmentUserordersBinding
    val viewModel by viewModels<UserOrdersViewModel>()
    val userOrderAdapter by lazy { UserOrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserordersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserOrdersRecycleView()

        lifecycleScope.launchWhenStarted {
            viewModel.allOrder.collectLatest {
                when (it) {
                    is Resource.Loading ->{
                        binding.progressbarAllOrders.visibility = View.VISIBLE

                    }
                    is Resource.Success ->{
                        binding.progressbarAllOrders.visibility = View.GONE
                        userOrderAdapter.differ.submitList(it.data)
                        if(it.data.isNullOrEmpty()){
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        }

                    }
                    is Resource.Error ->{
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()
                        binding.progressbarAllOrders.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        userOrderAdapter.onClick = {
            val action = UserOrderFragmentDirections.actionNavigationDashboardToOrderDetailFragment(it)
            findNavController().navigate(action)
        }

    }

    private fun setupUserOrdersRecycleView() {
        binding.rvAllOrders.apply {
            adapter = userOrderAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL, false)
        }
    }


}