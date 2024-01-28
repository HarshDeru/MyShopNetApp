package com.example.myshopnetapp.Activities.Fragments.Frag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myshopnetapp.Activities.Fragments.ViewModel.AddressViewModel
import com.example.myshopnetapp.Model.Address
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.databinding.FragmentUserAddressBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_address.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment: Fragment() {
    private lateinit var binding: FragmentUserAddressBinding
    val viewModel by viewModels<AddressViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progressBarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        binding.progressBarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()
                    }
                    is Resource.Error -> {
                        binding.progressBarAddress.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_SHORT).show()

                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonSaveAddress.setOnClickListener {
                val houseNumber = edit_addressLocation.text.toString()
                val fullName = edit_fullName.text.toString()
                val street = edit_street.text.toString()
                val mobileNumber = edit_phoneNumber.text.toString()
                val city = edit_city.text.toString()
                val postCode = edit_postCode.text.toString()
                val address = Address(houseNumber, fullName, street, mobileNumber, city, postCode)

                viewModel.addAddress(address)
            }
        }
    }
}