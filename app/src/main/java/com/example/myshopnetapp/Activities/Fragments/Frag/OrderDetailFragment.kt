package com.example.myshopnetapp.Activities.Fragments.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopnetapp.Adapters.BillingProductAdapter
import com.example.myshopnetapp.Model.OrderStatus
import com.example.myshopnetapp.Model.getOrderStatus
import com.example.myshopnetapp.Utilities.VerticalItemDecoration
import com.example.myshopnetapp.databinding.FragmentOrderDetailBinding

class OrderDetailFragment: Fragment() {
    private lateinit var binding: FragmentOrderDetailBinding
    private val billingProductAdapter by lazy {BillingProductAdapter()}
    private val args by navArgs<OrderDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order = args.order
        setupOrderDetailRecycleView()

        binding.apply {
            tvOrderId.text = "Order #${order.orderId}"

            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Shipped.status,
                    OrderStatus.Delivered.status,
                )
            )

            val currentOrderState = when(getOrderStatus(order.orderStatus)){
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Shipped -> 2
                is OrderStatus.Delivered ->3
                else -> 0
            }

            stepView.go(currentOrderState,false)
            if (currentOrderState == 1){
                stepView.done(true)
            }

            tvFullName.text = order.address.houseNumber
            tvAddress.text = "${order.address.fullName} ${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.mobileNumber
            tvTotalPrice.text = "£ ${order.totalPrice}"

        }
        billingProductAdapter.differ.submitList(order.product)
    }

    private fun setupOrderDetailRecycleView() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            addItemDecoration(VerticalItemDecoration())
        }
    }
}