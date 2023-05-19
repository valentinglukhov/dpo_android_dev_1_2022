package com.example.recyclerview_advanced.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recyclerview_advanced.R
import com.example.recyclerview_advanced.databinding.CharacterCardBinding

class CharacterAdapter(val context: Context) :
    PagingDataAdapter<Character, ViewHolder>(DiffUtilCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { character ->
            with(holder.binding) {
                Glide
                    .with(holder.itemView.context)
                    .load(character.image)
                    .into(avatar)
                name.text = context.getString(R.string.name, character.name)
                statusSpecies.text =
                    context.getString(R.string.status_and_species, character.status)
                lastKnownLocation.text =
                    context.getString(R.string.last_known_location, character.location.name)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CharacterCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}

class ViewHolder(val binding: CharacterCardBinding) : RecyclerView.ViewHolder(binding.root) {
}

class DiffUtilCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.name == newItem.name && oldItem.species == newItem.species
}