package com.lihkin16.foodit_3.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.R
import com.lihkin16.foodit_3.adapter.MenuAdapter
import com.lihkin16.foodit_3.databinding.FragmentSearchBinding
import com.lihkin16.foodit_3.model.MenuIItem


class SearchFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var menuItem: MutableList<MenuIItem>
    private lateinit var  databaseReference: DatabaseReference


    private lateinit var binding: FragmentSearchBinding
     private lateinit var adaper: MenuAdapter
    val MenuFoodName  =  listOf("Dosa", "Chowmin", "Momos", "Chilli Patato", "Kathi Roll", "Dosa", "Chowmin", "Momos" )
    val MenuFoodPrice = listOf("₹100", "₹200", "₹300", "₹400", "₹500","₹100", "₹200", "₹300", "₹400", "₹500")
    val MenuFoodImage = listOf(R.drawable.dosa, R.drawable.chowmin, R.drawable.momos, R.drawable.chillpatato, R.drawable.roll, R.drawable.dosa, R.drawable.chowmin, R.drawable.momos )
    val MenuFoodDes  =  listOf("Description", "Description", "Description", "Description", "Description","Description", "Description", "Description", "Description", "Description")





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val  filterMenufoodName  =   MenuFoodName.toMutableList()
    private val  filterMenufoodPrice  = MenuFoodPrice.toMutableList()
    private val  filterMenufoodImage  = MenuFoodImage.toMutableList()
    private val  filterMenufoodDes  = MenuFoodDes.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        databaseReference= FirebaseDatabase.getInstance().reference

        database = FirebaseDatabase.getInstance()
        menuItem = mutableListOf<MenuIItem>()



        retrieveData()


        binding = FragmentSearchBinding.inflate(inflater, container, false)





        //searchView

         setupSearchView()

        // all items



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
                showAllItems()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG" , error.toString())
            }

        })

    }

    private fun setAdapter() {
        adaper = MenuAdapter(menuItem , binding.root.context)
        binding.SearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.SearchRecyclerView.adapter = adaper

    }

    private fun showAllItems() {

        filterMenufoodName.clear()
        filterMenufoodPrice.clear()
        filterMenufoodImage.clear()
        filterMenufoodDes.clear()

        filterMenufoodName.addAll(MenuFoodName)
        filterMenufoodPrice.addAll(MenuFoodPrice)
        filterMenufoodImage.addAll(MenuFoodImage)
        filterMenufoodDes.addAll(MenuFoodDes)

        adaper.notifyDataSetChanged()




    }

    private fun setupSearchView() {

        val searchView = binding.searchView

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {


      filterMenufoodName.clear()
      filterMenufoodPrice.clear()
      filterMenufoodImage.clear()
      filterMenufoodDes.clear()

        MenuFoodName.forEachIndexed { index , s ->

           if( s.contains(query.orEmpty() , ignoreCase = true)){

               filterMenufoodName.add(s)
               filterMenufoodPrice.add(MenuFoodPrice[index])
               filterMenufoodImage.add(MenuFoodImage[index])
               filterMenufoodDes.add(MenuFoodDes[index])

           }

           }
        adaper.notifyDataSetChanged()
    }

    companion object {


    }
}