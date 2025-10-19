package com.example.catconnect.ui.adoption

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.catconnect.data.model.Adoption
import com.example.catconnect.databinding.ItemAdoptionBinding

class AdoptionAdapter(private val catList: List<Adoption>) : RecyclerView.Adapter<AdoptionAdapter.CatViewHolder>() {

    class CatViewHolder(private val binding: ItemAdoptionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Adoption) {
            binding.imgCat.load(cat.photoUrl) {
                crossfade(true)
            }
            binding.tvCatName.text = cat.name
            val catInfo = "Breed: ${cat.breed}, Age: ${cat.ageMonth} months"
            binding.tvCatInfo.text = catInfo

            binding.btnAdopt.setOnClickListener {
                // Aksi ketika tombol adopt di-klik
                Toast.makeText(itemView.context, "Adoption request for ${cat.name} sent!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = ItemAdoptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(catList[position])
    }

    override fun getItemCount() = catList.size
}
