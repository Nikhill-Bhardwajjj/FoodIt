package com.lihkin16.foodit_4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lihkin16.foodit_4.databinding.ActivityAddItemBinding
import com.lihkin16.foodit_4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


     private val binding: ActivityMainBinding by lazy {

         ActivityMainBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.MainAddMenu.setOnClickListener {

            startActivity(Intent(this, AddItemActivity::class.java))

        }

            binding.AllItems.setOnClickListener{

                startActivity(Intent(this , AllItemActivity::class.java))

            }

        binding.OutForDel.setOnClickListener {

            startActivity(Intent(this, OutForDeliveryActivity::class.java))
        }

        binding.AdminProfile.setOnClickListener {

            startActivity(Intent(this , AdminProfileActivity::class.java))
        }


        binding.AddUser.setOnClickListener {

            startActivity(Intent(this, AddUserActivity::class.java))


        }

        binding.PendingOrder.setOnClickListener {

            startActivity(Intent(this , PendingOrderActivity::class.java))
        }






    }
}