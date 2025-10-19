package com.example.catconnect.ui.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.catconnect.R
import com.example.catconnect.databinding.FragmentFindBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FindFragment : Fragment() {

    private var _binding: FragmentFindBinding? = null
    private val binding get() = _binding!!

    private val tabTitles = arrayOf("Events", "Adoption")
    private val tabIcons = arrayOf(R.drawable.ic_event_24, R.drawable.ic_pets_24)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup ViewPager dan Adapter
        val pagerAdapter = FindTabsAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        // ==========================================================
        // === TAMBAHKAN BARIS INI UNTUK MENONAKTIFKAN GESER/SWIPE ===
        binding.viewPager.isUserInputEnabled = false
        // ==========================================================

        // Hubungkan TabLayout dengan ViewPager
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
        }.attach()

        // Tambahkan listener untuk mengontrol visibilitas teks
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.text = tabTitles[tab.position]
                tab?.icon?.setTint(ContextCompat.getColor(requireContext(), android.R.color.black))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.text = null
                tab?.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.grey_medium))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tidak perlu melakukan apa-apa
            }
        })

        // Atur kondisi awal untuk tab pertama
        val firstTab = binding.tabs.getTabAt(0)
        firstTab?.text = tabTitles[0]
        firstTab?.icon?.setTint(ContextCompat.getColor(requireContext(), android.R.color.black))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
    