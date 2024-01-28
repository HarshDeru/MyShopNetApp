package com.example.myshopnetapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeViewPagerAdapter(
    private val fragments: List<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm,lifecycle){
    // Return the number of items in the list of Fragments
    override fun getItemCount(): Int {
        return fragments.size
    }

    // Create and return the Fragment to be displayed at the specified position
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}