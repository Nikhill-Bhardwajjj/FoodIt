package com.lihkin16.foodit_3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.databinding.ActivityDetailBinding
import com.lihkin16.foodit_3.model.CartItem
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    private var foodName : String? = null
    private var foodImage : String? = null
    private var foodPrice : String? = null
    private var foodDes : String? = null
    private var foodIngredient : String? = null
    private  lateinit  var auth : FirebaseAuth

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


         foodName = intent.getStringExtra("MenuName")
         foodPrice = intent.getStringExtra("MenuPrice")
         foodDes = intent.getStringExtra("MenuDes")
        foodIngredient = intent.getStringExtra("MenuIng")
        foodImage = intent.getStringExtra("MenuImage")


        

        binding.DetailFoodName.text = foodName
        Picasso.get().load(Uri.parse(foodImage)).into(binding.DetailFoodImage)
        binding.DetailDes.text = foodDes
        binding.DetailIngredient.text = foodIngredient



            binding.DetailBackButton.setOnClickListener{

                startActivity(Intent(this, MainActivity::class.java))


        }

        binding.DetailAddToCartButton.setOnClickListener{


          addtoCart()

        }


    }

    private fun addtoCart() {


        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""
        val cartRef = database.child("users").child(userId).child("cart")

        // Check if the item already exists in the cart
        cartRef.orderByChild("foodName").equalTo(foodName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Item already exists, update the quantity
                    for (itemSnapshot in dataSnapshot.children) {
                        val existingCartItem = itemSnapshot.getValue(CartItem::class.java)
                        existingCartItem?.let {
                            val newQuantity = it.foodQuantity?.plus(1)
                            itemSnapshot.ref.child("foodQuantity").setValue(newQuantity.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(binding.root.context, "Quantity updated in cart", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(binding.root.context, "Failed to update quantity in cart", Toast.LENGTH_SHORT).show()
                                }
                        }
                        return
                    }
                } else {
                    // Item doesn't exist, add it to the cart
                    val cartItem =
                        foodPrice?.toInt()?.let {
                            CartItem(foodName.toString(),  foodImage.toString(),
                                it, foodDes.toString(), "1" )
                        }
                    cartRef.push().setValue(cartItem)
                        .addOnSuccessListener {
                            Toast.makeText(binding.root.context, "Item added to cart", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(binding.root.context, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(binding.root.context, "Database error", Toast.LENGTH_SHORT).show()
            }
        })




    }
}