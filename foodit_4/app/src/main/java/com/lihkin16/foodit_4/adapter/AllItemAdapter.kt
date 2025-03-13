package com.lihkin16.foodit_4.adapter

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
import androidx.recyclerview.widget.RecyclerView
import com.lihkin16.foodit_4.databinding.ActivityAllItemBinding
import com.lihkin16.foodit_4.databinding.ItemAddBinding
import com.lihkin16.foodit_4.model.AllMenu
import com.squareup.picasso.Picasso

class AllItemAdapter(private val conext: Context,
                     private val menuList: ArrayList<AllMenu>
      ): RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>() {

    private val  itemAmounts = IntArray(menuList.size){1}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {

        return AllItemViewHolder(ItemAddBinding.inflate( LayoutInflater.from(parent.context) , parent, false))
    }

    override fun getItemCount(): Int {

        return  menuList.size

    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {


        holder.bind(position)
    }

    inner class AllItemViewHolder (private val binding:ItemAddBinding) : RecyclerView.ViewHolder(binding.root) {

        val imageView = binding.AllImage
        val cartAmout = binding.AllAmout

        fun bind(position: Int) {
            binding.apply {

                val itemAmount = itemAmounts[position]
                val menuItem = menuList[position]
                val url = menuItem.fooDImage
                val uri = Uri.parse(url)
                AllFoodName.text = menuItem.foodName
                AllFoodPrice.text = "₹"+ menuItem.foodPrice
                AllFoodDes.text = menuItem.foodDes




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

                AllAmout.text = itemAmount.toString()

                AllMinusButton.setOnClickListener {

                    decreaseQuantity(position)


                }

                AllPlusButton.setOnClickListener {

                    increaseQuantity(position)

                }
                AllDelete.setOnClickListener {

                    val itemPosition = adapterPosition
                    if(itemPosition != RecyclerView.NO_POSITION)
                    {
                        deleteItem(position)
                    }

                }






            }
        }
        private  fun decreaseQuantity(position: Int) {
            if (itemAmounts[position] > 1) {
                itemAmounts[position] = itemAmounts[position] - 1
                cartAmout.text = itemAmounts[position].toString()

            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemAmounts[position] < 10) {
                itemAmounts[position] = itemAmounts[position] + 1
                cartAmout.text = itemAmounts[position].toString()

            }
        }

        private fun deleteItem(position: Int) {
            menuList.removeAt(adapterPosition)

            notifyItemRemoved(adapterPosition)
            notifyItemRangeChanged(adapterPosition, menuList.size)

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