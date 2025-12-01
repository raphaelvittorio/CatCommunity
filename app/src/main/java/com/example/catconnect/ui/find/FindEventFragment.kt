package com.example.catconnect.ui.find

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.catconnect.databinding.FragmentFindEventBinding

class FindEventFragment : Fragment() {

    private var _binding: FragmentFindEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Data acara contoh (dummy data) dengan URL gambar HTTPS yang aman
        val eventList = listOf(
            FindEvent(
                id = "1",
                name = "Cat Lovers Community Gathering",
                photoUrl = "https://media.hswstatic.com/eyJidWNrZXQiOiJjb250ZW50Lmhzd3N0YXRpYy5jb20iLCJrZXkiOiJnaWZcL3NodXR0ZXJzdG9jay0yMjc4Nzc2MTg3LWhlcm8uanBnIiwiZWRpdHMiOnsicmVzaXplIjp7IndpZHRoIjo4Mjh9fX0=",
                location = "Central Park, Jakarta",
                time = "Saturday, 28 May 2024 - 15:00 WIB"
            ),
            FindEvent(
                id = "2",
                name = "Pameran Kucing Ras Internasional",
                photoUrl = "https://cdn.britannica.com/34/235834-050-C5843610/two-different-breeds-of-cats-side-by-side-outdoors-in-the-garden.jpg",
                location = "ICE BSD, Tangerang",
                time = "Sunday, 29 May 2024 - 10:00 WIB"
            ),
            FindEvent(
                id = "3",
                name = "Adopsi Kucing Jalanan",
                photoUrl = "https://plus.unsplash.com/premium_photo-1663127046003-ef3ec7ce7bc4?ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8Y3V0ZSUyMGNhdHN8ZW58MHx8MHx8fDA%3D&fm=jpg&q=60&w=3000",
                location = "Taman Suropati, Menteng",
                time = "Saturday, 4 June 2024 - 09:00 WIB"
            )
        )

        val adapter = FindEventAdapter(eventList)

        binding.rvEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvEvents.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvEvents)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
