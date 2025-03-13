package com.lihkin16.foodit_3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lihkin16.foodit_3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

   private lateinit var  binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var NavController: NavController = findNavController(R.id.fragmentContainerView)
        var bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(NavController)

        binding.NotificationButton.setOnClickListener {


            val bottomSheetDialog = BottomSheetNotification()

            bottomSheetDialog.show(supportFragmentManager, "bottomSheetDialog")

        }

    }
}