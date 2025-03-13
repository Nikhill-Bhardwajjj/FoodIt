package com.lihkin16.foodit_3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lihkin16.foodit_3.databinding.NotificationItemBinding

class NotificationAdapter(private var notificationList: ArrayList<String> , private var notificationImage : ArrayList<Int> ) :RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationViewHolder {


       val binding = NotificationItemBinding.inflate(
           LayoutInflater.from(parent.context),
           parent,false
       )

        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        holder.bind(position)
    }

    override fun getItemCount(): Int {
return notificationList.size   }

    inner class NotificationViewHolder( private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(position: Int) {

            binding.apply {
                NotificationTextView.text = notificationList[position]
                NotificationImage.setImageResource(notificationImage[position])

            }


        }

    }
}