package com.lihkin16.foodit_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lihkin16.foodit_4.adapter.PendingAdapter
import com.lihkin16.foodit_4.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {


    private  val binding: ActivityPendingOrderBinding by lazy{

        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val customerName = arrayListOf("Nikhil Bhardwaj" , "Gaurav Sharma " ," Harsh Sharma")
        val paymentStatus = arrayListOf("3" , "1" ," 4")
        val foodImage = arrayListOf(R.drawable.dosa , R.drawable.chillpatato , R.drawable.chowmin)



        val adapter = PendingAdapter(ArrayList(customerName) , ArrayList(paymentStatus) , ArrayList(foodImage) )
        binding.PendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.PendingOrderRecyclerView.adapter = adapter



    }
}