package com.lihkin16.foodit_3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lihkin16.foodit_3.databinding.ActivityPayoutBinding
import com.lihkin16.foodit_3.model.FoodItems
import com.lihkin16.foodit_3.model.OrderDetails
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PayoutActivity : AppCompatActivity() , PaymentResultListener {

    private lateinit var binding: ActivityPayoutBinding
    private lateinit var  auth: FirebaseAuth
    private lateinit var  name:String
    private lateinit var  address:String
    private lateinit var  email:String
    private lateinit var  phoneNo:String
    private lateinit var  totalAmount:String
    private lateinit var  foodName:ArrayList<String>
    private lateinit var  foodPrice:ArrayList<String>
    private lateinit var  foodImage:ArrayList<String>
    private lateinit var  foodDes:ArrayList<String>
    private lateinit var  foodQuantity:ArrayList<String>
    private lateinit var  totalPrice:String
    private lateinit var  database: DatabaseReference
    private lateinit var  userId: String
    private lateinit var  profileImage :String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Checkout.preload(applicationContext)
        val co = Checkout()

        co.setKeyID("rzp_test_N2AONTpr92LgUq")


        auth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance().getReference()

        totalPrice = intent.getStringExtra("totalPrice") ?:"0"
        foodName = intent.getStringArrayListExtra("foodName")  ?: arrayListOf()
        foodPrice = intent.getStringArrayListExtra("foodPrice") ?: arrayListOf()
        foodImage = intent.getStringArrayListExtra("foodImage")  ?: arrayListOf()
        foodDes = intent.getStringArrayListExtra("foodDes") ?: arrayListOf()
        foodQuantity = intent.getStringArrayListExtra("foodQuantity")  ?: arrayListOf()

        binding.TotalPrice.setText(totalPrice)

        setUserData()

            // get user details from database




        binding.PayoutBackButton.setOnClickListener {


            startActivity(Intent(this, MainActivity::class.java))

        }


        binding.PlaaceYourORderButton.setOnClickListener {

            name = binding.Name.text.toString().trim()
            address = binding.Address.text.toString().trim()
            email = binding.Email.text.toString().trim()
            phoneNo = binding.PhoneNo.text.toString().trim()

            if(name.isEmpty() || address.isEmpty() || email.isEmpty() || phoneNo.isEmpty())
            {

                Toast.makeText(this , "Enter all details" , Toast.LENGTH_SHORT).show()
                binding.Name.error = "Enter your name"
                binding.Address.error = "Enter your address"
                binding.Email.error = "Enter your email"
                binding.PhoneNo.error = "Enter your phone number"
                return@setOnClickListener
            }
            else{
                if(binding.payonline.isChecked)
                {
                   val priceInt = totalPrice.toDouble()
                    paymentGateWayStart(co, priceInt);
                }
                if(binding.payoffline.isChecked)
                {
                    placeOrder(false)
                }
            }






        }
    }

    private fun paymentGateWayStart(   co: Checkout, priceInt: Double) {

        val checkout = Checkout()
        val activity: Activity = this

        try{

            val options = JSONObject()
            options.put("name", "Foodit")
            options.put("description", "Payment")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#3399cc")
            options.put("currency", "INR")
            options.put("amount", (priceInt*100).toInt())

            val retryObj: JSONObject = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email","nikhillbhardwaj@gmail.com")
            prefill.put("contact","9310075326")

            options.put("prefill",prefill)
            checkout.open(activity, options)




        }
        catch(e: Exception)
        {

            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            return

        }




    }

    private fun placeOrder(paid: Boolean) {
        userId = auth.currentUser?.uid ?: ""
        val time = System.currentTimeMillis()
        val itemPushKey = database.child("orders").push().key

        val foodItemsList = foodName.indices.map { index ->
            FoodItems(
                imageUrl = foodImage.getOrNull(index) ?: "",
                name = foodName.getOrNull(index) ?: "",
                description = foodDes.getOrNull(index) ?: "",
                price = foodPrice.getOrNull(index) ?: "",
                quantity = foodQuantity.getOrNull(index) ?: ""
            )
        }

        val orderDetails = OrderDetails(
            userUid = userId,
            userName = name,
            userAddress = address,
            userEmail = email,
            userPhoneNo = phoneNo,
            totalPrice = totalPrice,
            paymentRecieved = paid,
            itemPushKey = itemPushKey,
            currentTime = time,
            orderAccepted = false,
            profileImage = profileImage,
            orderReceived = false,
            foodItems = foodItemsList
        )

        val orderReference: DatabaseReference = database.child("orders").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
                val bottomSheetDialog = CongratsBottomSheet()
                bottomSheetDialog.show(supportFragmentManager, "Your order")
                removeItemFromCart()
                addorderToHistory(orderDetails)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addorderToHistory(orderDetails: OrderDetails) {
        database.child("users").child(userId)
            .child("buyHistory").child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener { }
    }


    private fun removeItemFromCart() {
       val cartItemReference = database.child("users").child(userId).child("cart")
        cartItemReference.removeValue()
    }

    private fun setUserData() {

        val user = auth.currentUser
        if(user != null){

            val userId = user.uid
            val userRefrence = database.child("users").child(userId)

           userRefrence.addListenerForSingleValueEvent(object : ValueEventListener{
               override fun onDataChange(snapshot: DataSnapshot) {

                   if(snapshot.exists())
                   {
                        name  = snapshot.child("name").getValue(String::class.java)?:""
                        address  = snapshot.child("address").getValue(String::class.java)?:""
                        email  = snapshot.child("email").getValue(String::class.java)?:""
                        phoneNo  = snapshot.child("phoneNo").getValue(String::class.java)?:""
                        profileImage  = snapshot.child("profileImage").getValue(String::class.java)?:""
                   }

                   binding.apply {

                       Name.setText(name.toString())
                       Address.setText(address.toString())
                       Email.setText(email.toString())
                       PhoneNo.setText(phoneNo.toString())

                   }


               }

               override fun onCancelled(error: DatabaseError) {

               }

           })

 }
}

    override fun onPaymentSuccess(p0: String?) {

        Toast.makeText(this , "Payment Success" , Toast.LENGTH_SHORT).show() // payment success
        placeOrder(true)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this , "Payment failed" , Toast.LENGTH_SHORT).show()
    }

}