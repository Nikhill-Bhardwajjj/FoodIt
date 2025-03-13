package com.lihkin16.foodit_3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lihkin16.foodit_3.adapter.NotificationAdapter
import com.lihkin16.foodit_3.databinding.FragmentBottomSheetNotificationBinding


class BottomSheetNotification : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentBottomSheetNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetNotificationBinding.inflate(layoutInflater, container, false)

        val notificationList = listOf("Your order has been Canceled Successfully" ,"Congrats Your Order Placed" ,"Your order has been Canceled Successfully" , "Order has been taken by the driver", "Your order has been Canceled Successfully")
        val notificationImageList = listOf(R.drawable.nikhil, R.drawable.nikhil, R.drawable.nikhil, R.drawable.nikhil, R.drawable.nikhil)

        val adapter = NotificationAdapter(ArrayList(notificationList) , ArrayList(notificationImageList))

        binding.notificationRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        binding.notificationRecyclerView.adapter = adapter


        return binding.root
    }

    companion object {

    }
}