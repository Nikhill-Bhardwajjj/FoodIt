package com.lihkin16.foodit_4.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lihkin16.foodit_4.R
import com.lihkin16.foodit_4.databinding.ItemOutDeliveryBinding

class OutForDelAdapter( private val OutDelCustomerName: ArrayList<String> ,
                         private val OutDelPaymetStatus: ArrayList<String> , ): RecyclerView.Adapter<OutForDelAdapter.OutForDelViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutForDelViewHolder {

        return OutForDelViewHolder(ItemOutDeliveryBinding.inflate(LayoutInflater.from(parent.context) , parent , false))
    }

    override fun getItemCount(): Int {

        return  OutDelCustomerName.size

    }

    override fun onBindViewHolder(holder: OutForDelViewHolder, position: Int) {


        holder.bind(position)
    }

    inner class OutForDelViewHolder(private val binding: ItemOutDeliveryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {

                CostumerNameText.text = OutDelCustomerName[position]
                RecievedText.text = OutDelPaymetStatus[position]

                val colorMap = mapOf(
                    "Received" to Color.GREEN,
                    "Not Received" to Color.RED ,
                    "Pending" to  Color.GRAY

                )

                RecievedText.setTextColor(colorMap[OutDelPaymetStatus[position]]?: Color.BLACK)
                ReceiveedIndicator.backgroundTintList = ColorStateList.valueOf(colorMap[OutDelPaymetStatus[position]]?: Color.BLACK)

            }

        }



    }

}