package com.lihkin16.foodit_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lihkin16.foodit_4.adapter.OutForDelAdapter
import com.lihkin16.foodit_4.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {


    private  val binding: ActivityOutForDeliveryBinding by lazy {

        ActivityOutForDeliveryBinding.inflate(layoutInflater)


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val customerName = arrayListOf("Nikhil Bhardwaj" , "Gaurav Sharma " ," Harsh Sharma")
        val paymentStatus = arrayListOf("Received" , "Not Received" ," Pending")


        val adapter = OutForDelAdapter(ArrayList(customerName) , ArrayList(paymentStatus) )
        binding.OutDelRecycleView.layoutManager = LinearLayoutManager(this)
        binding.OutDelRecycleView.adapter = adapter

    }
}