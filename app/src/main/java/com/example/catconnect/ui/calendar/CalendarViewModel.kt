package com.example.catconnect.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catconnect.data.model.Event // Import the correct model
import java.util.Calendar

class CalendarViewModel : ViewModel() {

    // This is the single source of truth for all events.
    // It's private so only the ViewModel can modify it.
    private val _allEvents = mutableListOf<Event>()

    // The Fragment will observe this LiveData to get the events for the selected date.
    private val _eventsForSelectedDate = MutableLiveData<List<Event>>()
    val eventsForSelectedDate: LiveData<List<Event>> = _eventsForSelectedDate

    // Keep track of the selected date
    var selectedDate: Long = System.currentTimeMillis()
        set(value) {
            field = value
            loadEventsForDate(value)
        }

    // Corrected addEvent function
    fun addEvent(event: Event) {
        _allEvents.add(event)
        // After adding, refresh the list for the currently selected date
        loadEventsForDate(selectedDate)
    }

    fun deleteEvent(event: Event) {
        _allEvents.remove(event)
        // After deleting, refresh the list for the currently selected date
        loadEventsForDate(selectedDate)
    }

    fun loadEventsForDate(dateInMillis: Long) {
        val selectedCalendar = Calendar.getInstance().apply { timeInMillis = dateInMillis }

        val filteredEvents = _allEvents.filter { event ->
            // Use event.startAt instead of event.date
            val eventCalendar = Calendar.getInstance().apply { timeInMillis = event.startAt }
            selectedCalendar.get(Calendar.YEAR) == eventCalendar.get(Calendar.YEAR) &&
                    selectedCalendar.get(Calendar.DAY_OF_YEAR) == eventCalendar.get(Calendar.DAY_OF_YEAR)
        }
        // Use it.startAt instead of it.date
        _eventsForSelectedDate.value = filteredEvents.sortedBy { it.startAt }
    }
}
