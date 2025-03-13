package com.lihkin16.foodit_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_4.adapter.AllItemAdapter
import com.lihkin16.foodit_4.databinding.ActivityAllItemBinding
import com.lihkin16.foodit_4.model.AllMenu

class AllItemActivity : AppCompatActivity() {

    private lateinit var  databaseReference: DatabaseReference
    private lateinit var database : FirebaseDatabase
    private lateinit var MenuItem: ArrayList<AllMenu>

     private val    binding: ActivityAllItemBinding by lazy {

         ActivityAllItemBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference

        database = FirebaseDatabase.getInstance()
        MenuItem = ArrayList()


        interiveMenuItem()





    }

    private fun interiveMenuItem() {


        val foodRef : DatabaseReference = database.reference.child("Menu")

        // fetch data

           foodRef.addListenerForSingleValueEvent(object : ValueEventListener{

               override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                   MenuItem.clear()

                   //
                   for(foodSnap in snapshot.children){

                       val food = foodSnap.getValue(AllMenu::class.java)
                       food?.let {
                           MenuItem.add(it)
                       }
                   }
                   setAdapter()
               }

               override fun onCancelled(error: DatabaseError) {
                   Log.d("TAG" , error.toString())
               }

           })

    }

    private fun setAdapter() {

         val adapter = AllItemAdapter(this , MenuItem )
       binding.AllItemRecycleView.layoutManager = LinearLayoutManager(this)
       binding.AllItemRecycleView.adapter = adapter

    }
}