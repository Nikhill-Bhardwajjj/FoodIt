package com.lihkin16.foodit_3.adapter

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
import com.lihkin16.foodit_3.databinding.BuyAgainItemBinding
import com.squareup.picasso.Picasso

class BuyAgainAdapter(private val buyAgainFoodName:ArrayList<String>,
                      private val buyAgainFoodPrice:ArrayList<String>,
                      private val buyAgainFoodImage:ArrayList<Int>):RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {



    override fun onBindViewHolder(holder: BuyAgainViewHolder, position: Int) {

          holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImage[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyAgainViewHolder {

        val binding = BuyAgainItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BuyAgainViewHolder(binding)

    }

    override fun getItemCount(): Int = buyAgainFoodName.size


    class BuyAgainViewHolder  (private val binding:BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root)
    {

        val Image = binding.BuyAgainFoodImage
        fun bind(buyAgainFoodName:String,buyAgainFoodPrice:String,buyAgainFoodImage:Int)
        {
            binding.BuyAgainFoodName.text = buyAgainFoodName
            binding.BuyAgainFoodPrice.text = buyAgainFoodPrice
            //binding.BuyAgainFoodImage.setImageResource(buyAgainFoodImage)

            Picasso.get().load(buyAgainFoodImage)
                .resize(100, 100 )

                .into(Image , object : com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        val bitmap = (Image.drawable as BitmapDrawable).bitmap
                        val radius = 15
                        val roundedBitmap = getRoundedCornerBitmap(bitmap, radius)
                        Image.setImageBitmap(roundedBitmap)
                    }

                    override fun onError(e: Exception?) {

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
