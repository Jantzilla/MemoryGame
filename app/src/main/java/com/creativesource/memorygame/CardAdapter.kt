package com.creativesource.memorygame

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val cardCount: MutableList<Int>) :
    RecyclerView.Adapter<CardAdapter.MyViewHolder>() {

    class MyViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card, parent, false) as ImageView

        return MyViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.imageView.tag = cardCount[position]
    }

    override fun getItemCount() = cardCount.size
}