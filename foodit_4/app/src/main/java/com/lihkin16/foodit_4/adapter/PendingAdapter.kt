package com.lihkin16.foodit_4.adapter

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.lihkin16.foodit_4.databinding.ItemPendingBinding
import com.squareup.picasso.Picasso

class PendingAdapter(private  val customerName: ArrayList<String> ,
                     private val quantity: ArrayList<String> ,
                     private val foodImage: ArrayList<Int>): RecyclerView.Adapter <PendingAdapter.PendingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingViewHolder {

             return  PendingViewHolder (ItemPendingBinding.inflate(LayoutInflater
            .from(parent.context),
        parent,
        false))

    }

    override fun getItemCount(): Int {
        return customerName.size
    }

    override fun onBindViewHolder(holder: PendingViewHolder, position: Int) {

        holder.bind(position)
    }

    inner  class PendingViewHolder(private val binding: ItemPendingBinding) : RecyclerView.ViewHolder(binding.root) {

         private var  isAccepted = false
        fun bind(position: Int) {

            binding.apply {

                PendingCustomerName.text = customerName[position]
                PendingQuantity.text = quantity[position]

                Picasso.get().load(foodImage[position])
                    .resize(100, 100)
                    .into(pendingImage, object : com.squareup.picasso.Callback {
                        override fun onSuccess() {
                            val bitmap = (pendingImage.drawable as BitmapDrawable).bitmap
                            val radius = 20
                            val roundedBitmap = getRoundedCornerBitmap(bitmap, radius)
                            pendingImage.setImageBitmap(roundedBitmap)
                        }

                        override fun onError(e: Exception?) {

                        }

                    })

                PendingAcceptButton.apply {

                    if (!isAccepted) {

                        text = "Accept"
                    } else {
                        text = "Dispatch"
                    }
                    setOnClickListener {

                        if (!isAccepted) {

                            text = "Dispatch"
                            isAccepted = true

                            showToast("Order is Accepted")


                        } else {

                            customerName.removeAt(adapterPosition)

                            notifyItemRemoved(adapterPosition)
                            notifyDataSetChanged()
                            showToast("Order is Dispatched")

                        }
                    }
                }

            }
        }



        fun showToast ( message: String) {
            Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
        }



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