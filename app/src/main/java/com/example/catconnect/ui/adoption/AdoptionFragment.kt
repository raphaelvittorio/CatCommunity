package com.example.catconnect.ui.adoption

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.databinding.FragmentAdoptionBinding

class AdoptionFragment : Fragment() {

    private var _binding: FragmentAdoptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdoptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data adopsi dari repository
        val adoptionList = FakeRepository.getAdoptions()

        // Setup adapter dan RecyclerView
        val adapter = AdoptionAdapter(adoptionList)
        binding.rvAdoption.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
