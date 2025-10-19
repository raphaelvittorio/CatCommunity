package com.example.catconnect.ui.find

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load // Impor pustaka Coil
import com.example.catconnect.R

// 1. Model Data: Mendefinisikan seperti apa data sebuah "Event" itu
data class FindEvent(
    val id: String,
    val name: String,
    val photoUrl: String, // Sekarang kita akan gunakan ini
    val location: String,
    val time: String
)

// 2. Adapter untuk RecyclerView
class FindEventAdapter(private val events: List<FindEvent>) : RecyclerView.Adapter<FindEventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventIcon: ImageView = itemView.findViewById(R.id.event_icon)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val eventPhoto: ImageView = itemView.findViewById(R.id.event_photo)
        val eventLocation: TextView = itemView.findViewById(R.id.event_location)
        val eventTime: TextView = itemView.findViewById(R.id.event_time)
        val getDirectionButton: Button = itemView.findViewById(R.id.get_direction_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_find_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        // Mengikat data teks
        holder.eventName.text = event.name
        holder.eventLocation.text = event.location
        holder.eventTime.text = event.time
        
        // Menggunakan Coil untuk memuat gambar dari URL
        holder.eventPhoto.load(event.photoUrl) {
            crossfade(true) // Efek transisi halus
            placeholder(R.drawable.ic_launcher_background) // Gambar saat sedang memuat
        }

        // Mengatur ikon acara (bisa juga dimuat dari URL jika perlu)
        holder.eventIcon.setImageResource(R.drawable.ic_calendar)

        // Aksi tombol untuk navigasi ke halaman peta
        holder.getDirectionButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_findFragment_to_mapFragment)
        }
    }

    override fun getItemCount() = events.size
}