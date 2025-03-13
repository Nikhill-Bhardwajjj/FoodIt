package com.lihkin16.foodit_3.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.BottomSheetBuyAgain
import com.lihkin16.foodit_3.adapter.UserOrdersAdapter
import com.lihkin16.foodit_3.databinding.FragmentHistoryBinding
import com.lihkin16.foodit_3.model.OrderDetails


class HistoryFragment : Fragment() {

    private  lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: UserOrdersAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users").child(auth.currentUser?.uid!!).child("buyHistory")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)



        binding.PreviousPurchase.setOnClickListener {
            val  buyAgainBottomSheet = BottomSheetBuyAgain()
            buyAgainBottomSheet.show(parentFragmentManager, "BottomSheetDialog")
        }

        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UserOrdersAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        fetchOrderHistory()
    }

    private fun fetchOrderHistory() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orders = mutableListOf<OrderDetails>()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(OrderDetails::class.java)
                    order?.let { orders.add(it) }
                }
                adapter.updateOrders(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })
    }

}