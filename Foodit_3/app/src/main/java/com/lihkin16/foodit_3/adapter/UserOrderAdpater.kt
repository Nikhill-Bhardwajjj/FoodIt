package com.lihkin16.foodit_3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lihkin16.foodit_3.R
import com.lihkin16.foodit_3.model.OrderDetails

class UserOrdersAdapter(private var userOrders: List<OrderDetails>) :
    RecyclerView.Adapter<UserOrdersAdapter.UserOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_buyhistory, parent, false)
        return UserOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserOrderViewHolder, position: Int) {
        val userOrder = userOrders[position]
        holder.bind(userOrder)
    }

    override fun getItemCount() = userOrders.size

    fun updateOrders(orders: List<OrderDetails>) {
        userOrders = orders
        notifyDataSetChanged()
    }

    class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.OrderUserName)
        private val paymentStatus: TextView = itemView.findViewById(R.id.OrderPaidStatus)
        private val totalPrice: TextView = itemView.findViewById(R.id.OrderTotalPrice)
        private val foodItemsRecyclerView: RecyclerView = itemView.findViewById(R.id.FoodItemRecycleView)

        fun bind(userOrder: OrderDetails) {

            userName.text = userOrder.userName
            paymentStatus.text = if (userOrder.paymentRecieved) "Paid" else "Not Paid"
            totalPrice.text = userOrder.totalPrice

            foodItemsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            foodItemsRecyclerView.adapter = FoodItemsAdapter(userOrder.foodItems ?: emptyList())
        }
    }
}
