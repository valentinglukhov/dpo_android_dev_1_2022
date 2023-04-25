package com.example.m18_permissions.ui.main

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.m18_permissions.data.Sight
import com.example.m18_permissions.databinding.PhotoCardBinding

class PhotoAdapter(context: Context) : RecyclerView.Adapter<PhotoHolder>() {

    private var sightList: List<Sight> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val binding = PhotoCardBinding.inflate(LayoutInflater.from(parent.context))
        return PhotoHolder(binding)
    }

    override fun getItemCount(): Int {
        return sightList.size
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val currentSight = sightList.get(position)

        with(holder.binding) {
            Glide
                .with(holder.itemView.context)
                .load(Uri.parse(currentSight.photoUri))
                .into(photoView)
            textView.text = currentSight.date
        }
    }

    fun setData(sights: List<Sight>) {
        sightList = sights
        notifyDataSetChanged()
    }
}

class PhotoHolder(val binding: PhotoCardBinding) : RecyclerView.ViewHolder(binding.root)