package com.example.catconnect.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.catconnect.R
import com.example.catconnect.data.model.Event
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.ui.calendar.CalendarViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddEventFragment : Fragment(R.layout.fragment_add_event) {

    private val calendarViewModel: CalendarViewModel by activityViewModels()

    private var pickedDateMillis: Long? = null

    override fun onResume() {
        super.onResume()
        // Hide the main activity's action bar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        // Show the main activity's action bar again when leaving this fragment
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup the fragment's own toolbar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.add_event_toolbar)
        toolbar.setNavigationOnClickListener {
            // Navigate back when the back arrow is pressed
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDesc = view.findViewById<EditText>(R.id.etDesc)
        val etLocation = view.findViewById<EditText>(R.id.etLocation)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)
        val btnPickDate = view.findViewById<MaterialButton>(R.id.btnPickDate)
        val btnPickTime = view.findViewById<MaterialButton>(R.id.btnPickTime)
        val btnSave = view.findViewById<MaterialButton>(R.id.btnSave)

        val cal = Calendar.getInstance()

        btnPickDate.setOnClickListener {
            DatePickerDialog(requireContext(),
                { _, y, m, d ->
                    cal.set(Calendar.YEAR, y)
                    cal.set(Calendar.MONTH, m)
                    cal.set(Calendar.DAY_OF_MONTH, d)
                    pickedDateMillis = cal.timeInMillis
                    tvDate.text = Date(pickedDateMillis!!).toString()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnPickTime.setOnClickListener {
            TimePickerDialog(requireContext(),
                { _, h, min ->
                    val currentCal = Calendar.getInstance()
                    currentCal.timeInMillis = pickedDateMillis ?: System.currentTimeMillis()
                    currentCal.set(Calendar.HOUR_OF_DAY, h)
                    currentCal.set(Calendar.MINUTE, min)
                    currentCal.set(Calendar.SECOND, 0)
                    currentCal.set(Calendar.MILLISECOND, 0)
                    pickedDateMillis = currentCal.timeInMillis
                    tvDate.text = Date(pickedDateMillis!!).toString()
                },
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true
            ).show()
        }

        btnSave.setOnClickListener {
            val title = etTitle.text?.toString()?.trim().orEmpty()
            val desc = etDesc.text?.toString()?.trim().orEmpty()
            val locName = etLocation.text?.toString()?.trim().orEmpty()
            val startAt = pickedDateMillis
            val currentUser = FakeRepository.currentUser

            if (title.isBlank() || desc.isBlank() || locName.isBlank() || startAt == null) {
                Snackbar.make(view, "Please fill all fields and pick a date/time", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            if (currentUser == null) {
                Snackbar.make(view, "Cannot create event: User not found", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newEvent = Event(
                id = "e${System.currentTimeMillis()}",
                title = title,
                desc = desc,
                startAt = startAt,
                locationName = locName,
                bannerUrl = "https://picsum.photos/seed/${System.currentTimeMillis()}/800/500",
                organizerId = currentUser.id,
                attendees = mutableListOf(currentUser.id) // Organizer is the first attendee
            )

            calendarViewModel.addEvent(newEvent)

            Snackbar.make(view, "Event saved", Snackbar.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
