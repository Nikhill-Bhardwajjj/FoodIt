package com.lihkin16.foodit_3.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.DetailActivity
import com.lihkin16.foodit_3.databinding.MenuItemBinding
import com.lihkin16.foodit_3.model.CartItem
import com.lihkin16.foodit_3.model.MenuIItem
import com.squareup.picasso.Picasso

class MenuAdapter(
    private val menuList: List<MenuIItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemAmounts = IntArray(menuList.size) { 1 }
    private  lateinit  var auth : FirebaseAuth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        auth= FirebaseAuth.getInstance()

        return MenuViewHolder(
            MenuItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {

        return menuList.size
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {


        /*val itemName = items[position]
        val itemImage = image[position]
        val itemPrice = price[position]
        val itemDescription = description[position]*/

        holder.bind(position)

        holder.itemView.setOnClickListener {

            openDetails(position)


        }
    }

    private fun openDetails(position: Int) {


        val menuItem = menuList[position]
        //
        val intent = Intent(requireContext, DetailActivity::class.java)
        intent.putExtra("MenuName", menuItem.foodName)
        /*val uri = Uri.parse(menuItem.fooDImage)*/
        intent.putExtra("MenuImage", menuItem.fooDImage)
        intent.putExtra("MenuPrice", menuItem.foodPrice)
        intent.putExtra("MenuDes", menuItem.foodDes)
        intent.putExtra("MenuIng", menuItem.foodIngredient)
        requireContext.startActivity(intent)


    }


    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val imageView = binding.MenuFoodImage
        private val addToCartButton = binding.MenuCart



        init {

            addToCartButton.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    addtocart(menuList[position])
                }
            }
        }


        fun bind(position: Int) {

            binding.MenuFoodName.text = menuList[position].foodName
            binding.MenuFoodPrice.text = "₹" + menuList[position].foodPrice
            binding.MenuFoodDes.text = menuList[position].foodDes
            val uri = Uri.parse(menuList[position].fooDImage)

            //setOnclick to open Details


            Picasso.get().load(uri)
                .resize(100, 100)

                .into(imageView, object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                        val radius = 20
                        val roundedBitmap = getRoundedCornerBitmap(bitmap, radius)
                        imageView.setImageBitmap(roundedBitmap)
                    }

                    override fun onError(e: Exception?) {

                    }

                })


        }

        private fun getRoundedCornerBitmap(bitmap: Bitmap, radius: Int): Bitmap {
            val roundedBitmap =
                Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(roundedBitmap)

            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            val rect = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
            canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)

            return roundedBitmap
        }
    }

    private fun addtocart(menuIItem: MenuIItem) {

        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid ?: ""
        val cartRef = database.child("users").child(userId).child("cart")

        // Check if the item already exists in the cart
        cartRef.orderByChild("foodName").equalTo(menuIItem.foodName).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Item already exists, update the quantity
                    for (itemSnapshot in dataSnapshot.children) {
                        val existingCartItem = itemSnapshot.getValue(CartItem::class.java)
                        existingCartItem?.let {
                            val currentQuantity = it.foodQuantity?.toInt() ?: 0
                            val newQuantity = currentQuantity + 1
                            itemSnapshot.ref.child("foodQuantity").setValue(newQuantity.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext, "Quantity updated in cart", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext, "Failed to update quantity in cart", Toast.LENGTH_SHORT).show()
                                }
                        }
                        return
                    }
                }else{
                    val cartItem = menuIItem.foodPrice?.let {
                        CartItem(
                            menuIItem.foodName,
                            menuIItem.fooDImage,
                            it.toInt(),
                            menuIItem.foodDes,
                            "1"
                        )
                    }
                    cartRef.push().setValue(cartItem)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext, "Item added to cart", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(requireContext, "Failed to add item to cart", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(requireContext, "Database error", Toast.LENGTH_SHORT).show()
            }
        })




    }

    interface OnClickListener {

        fun onItemClick(position: Int)


    }


}


