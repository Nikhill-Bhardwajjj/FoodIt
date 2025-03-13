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
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lihkin16.foodit_3.DetailActivity
import com.lihkin16.foodit_3.databinding.PopularItemBinding
import com.squareup.picasso.Picasso

class PopularAdapter(
    private val items: List<String>,
    private val image: List<Int>,
    private val price: List<String>,
    private val description: List<String>,
    val requireContext: Context
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {


        return PopularViewHolder(
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {


        val itemName = items[position]
        val itemImage = image[position]
        val itemPrice = price[position]
        val itemDescription = description[position]

        holder.bind(itemName, itemImage, itemPrice, itemDescription)

        holder.itemView.setOnClickListener {
            val intent = Intent(requireContext, DetailActivity::class.java)
            intent.putExtra("MenuName", itemName)
            intent.putExtra("MenuImage", itemImage)
            intent.putExtra("MenuPrice", itemPrice)
            intent.putExtra("MenuDes", itemDescription)
            requireContext.startActivity(intent)
        }
    }

    class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.popularFoodImage
        fun bind(items: String, images: Int, prices: String, description: String) {

            binding.popularFoodName.text = items
            binding.popularFoodPrice.text = prices
            binding.popularFoodDes.text = description


            Picasso.get().load(images)
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


    override fun getItemCount(): Int {

        return items.size

    }

}