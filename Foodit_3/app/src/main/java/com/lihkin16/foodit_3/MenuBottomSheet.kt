package com.lihkin16.foodit_3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.adapter.MenuAdapter
import com.lihkin16.foodit_3.databinding.BottomSheetMenuBinding
import com.lihkin16.foodit_3.model.MenuIItem


class MenuBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMenuBinding

   private lateinit var database: FirebaseDatabase
   private lateinit var menuItem: MutableList<MenuIItem>
    private lateinit var  databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BottomSheetMenuBinding.inflate(inflater, container, false)

        binding.BackButton.setOnClickListener {

            dismiss()

        }

        databaseReference= FirebaseDatabase.getInstance().reference

        database = FirebaseDatabase.getInstance()
        menuItem = mutableListOf<MenuIItem>()

        retrieveData()



        return binding.root

    }

    private fun retrieveData() {

        val foodRef : DatabaseReference = database.reference.child("Menu")

        // fetch data

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {

                menuItem.clear()

                //
                for(foodSnap in snapshot.children){

                    val food = foodSnap.getValue(MenuIItem::class.java)
                    food?.let {
                        menuItem.add(it)
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
        val adapter = MenuAdapter(menuItem , requireContext())
        binding.MenuRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.MenuRecycler.adapter = adapter
    }

    companion object {

            }
    }
