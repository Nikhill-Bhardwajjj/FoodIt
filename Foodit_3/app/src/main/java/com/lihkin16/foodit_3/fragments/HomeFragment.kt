package com.lihkin16.foodit_3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.MenuBottomSheet
import com.lihkin16.foodit_3.R
import com.lihkin16.foodit_3.adapter.MenuAdapter
import com.lihkin16.foodit_3.databinding.FragmentHomeBinding
import com.lihkin16.foodit_3.model.MenuIItem


class HomeFragment : Fragment() {

    private lateinit var database : FirebaseDatabase
    private lateinit var  menuItem:MutableList<MenuIItem>

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.textView12.setOnClickListener {

            val menuBottomSheet = MenuBottomSheet()
            menuBottomSheet.show(parentFragmentManager, "BottomSheetDialog")
        }

        database = FirebaseDatabase.getInstance()
        menuItem = mutableListOf()


        retrieveDisplayPopularItems()

        return binding.root



    }

    private fun retrieveDisplayPopularItems() {

        database = FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference = database.getReference("Menu")
        menuItem = mutableListOf()

        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (foodSnapshot in snapshot.children) {
                        val food = foodSnapshot.getValue(MenuIItem::class.java)
                        food?.let { menuItem.add(it) }
                    }
                }
                // display

                randomPopularItems()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }




    })
    }

    private fun randomPopularItems() {
        // create a suffle List of menu items

        val index  = menuItem.indices.toList().shuffled()
        val numIItem  = 6

        val popularItems = index.take(numIItem).map { menuItem[it] }
        val adapter = MenuAdapter(popularItems, requireContext())
        binding.popularRecycler.adapter = adapter
        binding.popularRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val  imageList = ArrayList<SlideModel>() // Create image list</>
        imageList.add(SlideModel(R.drawable.dosa, ScaleTypes.FIT )) // Add your image in list
        imageList.add(SlideModel(R.drawable.chillpatato, ScaleTypes.FIT ))
        imageList.add(SlideModel(R.drawable.chowmin, ScaleTypes.FIT ))
        imageList.add(SlideModel(R.drawable.chillpatato, ScaleTypes.FIT ))
        imageList.add(SlideModel(R.drawable.dosa, ScaleTypes.FIT ))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList , ScaleTypes.FIT)

        imageSlider.setItemClickListener(object : ItemClickListener {



            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {


                val itemPosition = imageList[position]
                val itemMessage = "Selected Image is of $position"
                Toast.makeText(context, itemMessage, Toast.LENGTH_SHORT).show()
            }
        })









    }

}