package com.creativesource.memorygame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card.view.*

class CardAdapter(private val clickListener: ClickListener, private val cardCount: MutableList<Int>) : RecyclerView.Adapter<CardAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.tv_card.text = cardCount[position].toString()
        holder.itemView.setOnClickListener {
            clickListener.onClickListener(cardCount[position])
        }
    }

    override fun getItemCount() = cardCount.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}