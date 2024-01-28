package com.example.myshopnetapp.Activities.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myshopnetapp.Activities.Fragments.Categories.*
import com.example.myshopnetapp.Activities.RegisterActivity
import com.example.myshopnetapp.Activities.SettingsActivity
import com.example.myshopnetapp.Adapters.HomeViewPagerAdapter
import com.example.myshopnetapp.R
import com.example.myshopnetapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_settings.*

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nav_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //when specific id is clicked then run the process
    //if setting icon is clicked then open setting activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //DEFAULT CODE//
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        homeViewModel.text.observe(viewLifecycleOwner) {
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding != null
    }
    //the code above are default.

    //the onViewCreate will display different category's fragment under the Tab Layout view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategory(),
            HoodiesFragment(),
            JacketsFragment(),
            TShirtFragment(),
            JeansFragment(),
            JoggersFragment(),
            ShirtsFragment()
        )

        //cancels the scroll behaviou that goes to next fragments
        binding.optionViewPageHome.isUserInputEnabled = false

        //adapter is being used to display the category as expected
        val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)
        _binding.optionViewPageHome.adapter = viewPager2Adapter
        TabLayoutMediator(_binding.optionTabLayout, _binding.optionViewPageHome){tab, position ->
            when(position){
                0 -> tab.text = "Home"
                1 -> tab.text = "Hoodies"
                2 -> tab.text = "Jackets"
                3 -> tab.text = "T-Shirts"
                4 -> tab.text = "Jeans"
                5 -> tab.text = "Joggers"
                6 -> tab.text = "Shirts"
            }
        }.attach()
    }






}