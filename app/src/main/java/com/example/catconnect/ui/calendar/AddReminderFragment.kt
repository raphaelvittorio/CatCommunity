package com.example.catconnect.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.catconnect.MainActivity
import com.example.catconnect.databinding.FragmentAddReminderBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class AddReminderFragment : Fragment() {

    private var _binding: FragmentAddReminderBinding? = null
    private val binding get() = _binding!!

    private var selectedHour: Int = -1
    private var selectedMinute: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.showAppBar(false)
    }

    override fun onPause() {
        super.onPause()
        (activity as? MainActivity)?.showAppBar(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.timeButton.setOnClickListener {
            showTimePicker()
        }

        binding.saveButton.setOnClickListener {
            saveReminder()
        }
    }

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Pilih Waktu Pengingat")
            .build()

        picker.addOnPositiveButtonClickListener {
            selectedHour = picker.hour
            selectedMinute = picker.minute
            // Tampilkan waktu yang dipilih di tombol
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, selectedHour)
                set(Calendar.MINUTE, selectedMinute)
            }
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            binding.timeButton.text = timeFormat.format(calendar.time)
        }

        picker.show(childFragmentManager, "time_picker")
    }

    private fun saveReminder() {
        val name = binding.nameEditText.text.toString().trim()
        val location = binding.placeEditText.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Nama pengingat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        if (location.isEmpty()) {
            Toast.makeText(requireContext(), "Tempat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedHour == -1 || selectedMinute == -1) {
            Toast.makeText(requireContext(), "Silakan pilih waktu terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        // Kirim data kembali ke CalendarFragment
        setFragmentResult("add_reminder_request", bundleOf(
            "name" to name,
            "location" to location,
            "hour" to selectedHour,
            "minute" to selectedMinute
        ))

        // Kembali ke halaman sebelumnya
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
