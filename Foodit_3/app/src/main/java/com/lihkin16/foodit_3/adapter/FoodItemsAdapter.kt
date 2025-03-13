package com.lihkin16.foodit_3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lihkin16.foodit_3.R
import com.lihkin16.foodit_3.model.FoodItems

class FoodItemsAdapter(private val foodItems: List<FoodItems>) :
    RecyclerView.Adapter<FoodItemsAdapter.FoodItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.buy_again_item, parent, false)
        return FoodItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = foodItems[position]
        holder.bind(foodItem)
    }

    override fun getItemCount() = foodItems.size

    class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodImage: ImageView = itemView.findViewById(R.id.BuyAgainFoodImage)
        private val foodName: TextView = itemView.findViewById(R.id.BuyAgainFoodName)

        private val foodPrice: TextView = itemView.findViewById(R.id.BuyAgainFoodPrice)

        fun bind(foodItem: FoodItems) {
            Glide.with(itemView.context).load(foodItem.imageUrl).into(foodImage)
            foodName.text = foodItem.name
            foodPrice.text = foodItem.price
        }
    }
}
