package com.lihkin16.foodit_3.adapter

import android.content.Context
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.databinding.CartItemBinding
import com.lihkin16.foodit_3.model.CartItem
import com.squareup.picasso.Picasso



class CartAdapter(private val  context:Context ,
                  private val cartItems: List<CartItem>
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    interface CartItemListener{
        fun onCartItemChanged()
    }


   private var listener: CartItemListener? = null

    fun setCartAdapterListener(listener: CartItemListener) {
        this.listener = listener
    }

    private fun notifyCartItemsChanged() {
        listener?.onCartItemChanged()
    }

 private val  itemAmounts = IntArray(cartItems.size){1}
    private  val auth = FirebaseAuth.getInstance()
    private val mutableCartItems: MutableList<CartItem> = cartItems.toMutableList()


    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid?:""
        val cartItemNumber = cartItems.size
        itemQuantity = IntArray(cartItemNumber){1}
        carIItemRefrence = database.reference.child("users").child(userId).child("cart")


    }

    fun calculateTotalPrice(): Int {
        var totalPrice = 0
        for (cartItem in mutableCartItems) {
            totalPrice += cartItem.foodPrice * (cartItem.foodQuantity?.toInt() ?: 0)
        }
        return totalPrice
    }

    companion object {
        private  var itemQuantity  : IntArray = intArrayOf()
        private lateinit var  carIItemRefrence : DatabaseReference
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        return CartViewHolder(CartItemBinding.inflate( LayoutInflater.from(parent.context) , parent, false))
    }

    override fun getItemCount(): Int {

        return mutableCartItems.size

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {


        holder.bind(mutableCartItems[position])
    }

    fun getUpdatedQuantity(): MutableList<String>{

       val itemQuantityList = mutableListOf<String>()
        itemQuantityList.addAll(cartItems.map { it.foodQuantity.toString() })
        return itemQuantityList
    }



    inner class CartViewHolder (private val binding:CartItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val imageView = binding.cartImage
        val cartAmout = binding.cartAmout

        fun bind(cartItem: CartItem) {
            binding.apply {

                cartFoodName.text = cartItem.foodName
                cartFoodPrice.text = "₹${cartItem.foodPrice}"
                cartFoodDes.text = cartItem.foodDes
                val uri = Uri.parse(cartItem.foodImage)




                Picasso.get().load(uri)
                    .resize(100, 100)
                    .into(imageView , object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                            val radius = 20
                            val roundedBitmap = getRoundedCornerBitmap(bitmap, radius)
                            imageView.setImageBitmap(roundedBitmap)
                        }

                        override fun onError(e: Exception?) {

                        }

                        })

                cartAmout.text = cartItem.foodQuantity.toString() //itemAmount.toString()

                cartMinusButton.setOnClickListener {

                    decreaseQuantity(adapterPosition)


                }

                cartPlusButton.setOnClickListener {

                    increaseQuantity(adapterPosition)

                }
                cartDelete.setOnClickListener {

                    val itemPosition = adapterPosition
                    if(itemPosition != RecyclerView.NO_POSITION)
                    {
                        deleteItem(adapterPosition)
                    }

                }






        }
    }
        private fun decreaseQuantity(position: Int) {
            val currentQuantityString = mutableCartItems[position].foodQuantity
            val currentQuantity = currentQuantityString?.toInt()

            if (currentQuantity != null && currentQuantity > 0) {
                val newQuantity = currentQuantity - 1
                updateQuantityInDatabase(position, newQuantity)
                listener?.onCartItemChanged()
                notifyCartItemsChanged()
            }
        }

        private fun increaseQuantity(position: Int) {
            val currentQuantityString = mutableCartItems[position].foodQuantity
            val currentQuantity = currentQuantityString?.toInt()

            if (currentQuantity != null && currentQuantity >= 0) {
                val newQuantity = currentQuantity + 1
                updateQuantityInDatabase(position, newQuantity)
                listener?.onCartItemChanged()
                notifyCartItemsChanged()
            }
        }

        private fun updateQuantityInDatabase(position: Int, newQuantity: Int) {
            val uniqueKey = getUniqueKey(position) { uniqueKey ->
                uniqueKey?.let {
                    carIItemRefrence.child(it).child("foodQuantity")
                        .setValue(newQuantity.toString())
                        .addOnSuccessListener {
                            mutableCartItems[position].foodQuantity = newQuantity.toString()
                            cartAmout.text = newQuantity.toString()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }




        private fun deleteItem(position: Int) {


            val position = adapterPosition
            getUniqueKey(position){uniqueKey ->
                if(uniqueKey != null){

                    removeItem(position, uniqueKey)
                    listener?.onCartItemChanged()
                }
            }


        }

        private fun removeItem(position: Int, uniqueKey: String) {

            if (uniqueKey!= null) {
                carIItemRefrence.child(uniqueKey).removeValue().addOnSuccessListener {
                    mutableCartItems.removeAt(position)
                    /*cartItemImage.removeAt(position)
                    cartItemPrice.removeAt(position)
                    cartDes.removeAt(position)*/
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, mutableCartItems.size)
                    Toast.makeText(context , "Item Remove Successfully" , Toast.LENGTH_SHORT).show()
                    itemQuantity = itemQuantity.filterIndexed { index, i -> index != position }.toIntArray()
                }
                    .addOnFailureListener  {

                        Toast.makeText(context , "Failed to delete item" , Toast.LENGTH_SHORT).show()

                    }
            }

        }

        private fun getUniqueKey(position: Int , onComplete:(String?) -> Unit ) {

            carIItemRefrence.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        var uniqueKey:String?= null

                        snapshot.children.forEachIndexed { index, dataSnapshot ->

                            if(index == position){
                                uniqueKey = dataSnapshot.key
                                return@forEachIndexed
                            }
                        }

                        onComplete(uniqueKey)

                    }
                    }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })



        }




        private fun getRoundedCornerBitmap(bitmap: Bitmap, radius: Int): Bitmap {
            val roundedBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(roundedBitmap)

            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            val rect = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
            canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)

            return roundedBitmap
        }





    }



}