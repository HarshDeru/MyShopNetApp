package com.example.myshopnetapp.Activities.ui.Cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshopnetapp.Adapters.CartProductAdapter
import com.example.myshopnetapp.DatabaseFirestore.FirestoreCart
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.Utilities.VerticalItemDecoration
import com.example.myshopnetapp.databinding.FragmentCartBinding
import kotlinx.coroutines.flow.collectLatest

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRecycleView()

        var totalPrice = 0f

        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest { price->
                price?.let {
                    totalPrice = it
                    binding.textViewCartTotalPrice.text = "$ $price"
                }
            }
        }

        cartAdapter.onProductClick = {
            val b = Bundle().apply { putParcelable("product",it.product) }
            findNavController().navigate(R.id.action_navigation_notifications_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick = {
            viewModel.changeQuantity(it,FirestoreCart.QuantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick = {
            viewModel.changeQuantity(it,FirestoreCart.QuantityChanging.DECREASE)
        }

        binding.buttonCheckout.setOnClickListener {
            var action = CartFragmentDirections.actionNavigationNotificationsToBillingFragment(totalPrice,cartAdapter.differ.currentList.toTypedArray())
            findNavController().navigate(action)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item from your cart?")
                    setNegativeButton("Cancel"){ dialog,_ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){ dialog,_ ->
                        viewModel.deleteCartProducts(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }

        }


        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading ->{
                        binding.progressBarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()){
                            showBlankCart()
                            hideOtherViews()
                        }else{
                            hideBlankCart()
                            cartAdapter.differ.submitList(it.data)
                            showOtherViews()
                        }
                    }
                    is Resource.Error ->{
                        binding.progressBarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            recycleViewCartProduct.visibility = View.GONE
            constraintTotal.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun showOtherViews() {
        binding.apply {
            recycleViewCartProduct.visibility = View.VISIBLE
            constraintTotal.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun showBlankCart() {
        binding.apply {
            imageViewEmptyBox.visibility = View.VISIBLE
        }
    }

    private fun hideBlankCart() {
        binding.apply {
            imageViewEmptyBox.visibility = View.GONE
        }
    }

    private fun setupCartRecycleView(){
        binding.recycleViewCartProduct.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }


}