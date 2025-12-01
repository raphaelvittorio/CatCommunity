package com.example.catconnect.ui.find

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.catconnect.ui.adoption.AdoptionFragment

class FindTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FindEventFragment()
            1 -> AdoptionFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
