package com.lihkin16.foodit_3.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.PayoutActivity
import com.lihkin16.foodit_3.adapter.CartAdapter
import com.lihkin16.foodit_3.databinding.FragmentCartBinding
import com.lihkin16.foodit_3.model.CartItem


class CartFragment : Fragment()  , CartAdapter.CartItemListener{

    private lateinit var binding: FragmentCartBinding
    private  lateinit var  cartItem: MutableList<String>
    private lateinit var cartItemPrice: MutableList<String>
    private lateinit var cartItemImage: MutableList<String>
    private lateinit var cartDes: MutableList<String>
    private lateinit var cartItemQuantity: MutableList<String>
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adapter: CartAdapter
    private lateinit var userId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentCartBinding.inflate(inflater, container, false)

         auth = FirebaseAuth.getInstance()
        retrieveDisplayCartItems()

        binding.orderButton.setOnClickListener{

            getOrderDetails()
            startActivity(Intent(requireContext(), PayoutActivity::class.java))
        }



        return binding.root
    }

    private fun getOrderDetails() {
        val orderIdReference :DatabaseReference = database.reference.child("users").child(userId).child("cart")
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<Int>()
        val foodImage = mutableListOf<String>()
        val foodDes = mutableListOf<String>()
        val foodQuantity = adapter.getUpdatedQuantity()
        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                for (foodSnapshot in snapshot.children) {
                    val orderItem = foodSnapshot.getValue(CartItem::class.java)
                    foodName.add(orderItem?.foodName ?: "")
                    foodPrice.add(orderItem?.foodPrice ?: 0)
                    foodImage.add(orderItem?.foodImage ?: "")
                    foodDes.add(orderItem?.foodDes ?: "")
                    /*foodQuantity.add(orderItem?.foodQuantity ?: "")*/

                }
               orderNow(foodName,foodPrice,foodImage,foodDes,foodQuantity)
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(context, "Something went wrong , Please try again later", Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun orderNow(foodName: MutableList<String>, foodPrice: MutableList<Int>, foodImage: MutableList<String>, foodDes: MutableList<String>, foodQuantity: MutableList<String>) {

                 if(isAdded && context != null) {
                     val intent = Intent(context, PayoutActivity::class.java)
                     intent.putExtra("foodName", foodName as ArrayList<String>  ?)
                     intent.putExtra("foodPrice", foodPrice as ArrayList<Int> ?)
                     intent.putExtra("foodImage", foodImage as ArrayList<String> ?)
                     intent.putExtra("foodDes", foodDes as ArrayList<String> ?)
                     intent.putExtra("foodQuantity", foodQuantity as ArrayList<String> ?)
                     intent.putExtra("totalPrice" , binding.TotalFoodPrice.text as String)
                     startActivity(intent)
                 }


    }


    private fun retrieveDisplayCartItems() {
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodReference: DatabaseReference = database.getReference("users").child(userId).child("cart")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartItem>()
                snapshot.children.forEach { foodSnapshot ->
                    val item = foodSnapshot.getValue(CartItem::class.java)
                    item?.let { cartItems.add(it) }
                }
                setAdapter(cartItems)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCartItemChanged() {
        updateCartUI()
    }
    private fun setAdapter(cartItems: List<CartItem>) {
         adapter = CartAdapter(requireContext(), cartItems)
        adapter.setCartAdapterListener(this)
        binding.CartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.CartRecyclerView.adapter = adapter

        val foodPrice = adapter.calculateTotalPrice()
        binding.FoodPrice.text = "$foodPrice"
        val foodDiscount = foodPrice*0.2
        binding.FoodDiscount.text = "$foodDiscount"
        val totalPrice = foodPrice - foodDiscount
        binding.TotalFoodPrice.text = "$totalPrice"

    }





    private fun updateCartUI() {
        val foodPrice = adapter.calculateTotalPrice()
        binding.FoodPrice.text = "$foodPrice"
        val foodDiscount = foodPrice * 0.2
        binding.FoodDiscount.text = "$foodDiscount"
        val totalPrice = foodPrice - foodDiscount
        binding.TotalFoodPrice.text = "$totalPrice"
    }


}






