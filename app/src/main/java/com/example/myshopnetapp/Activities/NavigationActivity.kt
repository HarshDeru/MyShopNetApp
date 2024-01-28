package com.example.myshopnetapp.Activities

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myshopnetapp.Activities.ui.Cart.CartViewModel
import com.example.myshopnetapp.Activities.ui.Order.UserOrderFragment
import com.example.myshopnetapp.R
import com.example.myshopnetapp.Utilities.Resource
import com.example.myshopnetapp.databinding.ActivityNavigationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class NavigationActivity : BaseActivity() {

private lateinit var binding: ActivityNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityNavigationBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //CODE ABOVE ARE ADDED DEFAULT//


        val viewModel by viewModels<CartViewModel>()

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Success ->{
                        val count = it.data?.size?: 0
                        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_view)
                        bottomNavigation.getOrCreateBadge(R.id.navigation_notifications).apply {
                            number = count
                            backgroundColor = resources.getColor(R.color.pinkThemeColor)
                        }
                    }
                    else -> Unit
                }
            }
        }

    }

    //CODE BELOW ARE ADDED DEFAULT//
    override fun onBackPressed() {
        clickDoubleBackToExit()
    }



}