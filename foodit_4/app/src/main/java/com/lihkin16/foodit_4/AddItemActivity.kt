package com.lihkin16.foodit_4

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.lihkin16.foodit_4.databinding.ActivityAddItemBinding
import com.lihkin16.foodit_4.model.AllMenu



class AddItemActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDes: String
    private lateinit var foodIngredient: String
    private     var image: Uri?= null


     private val binding: ActivityAddItemBinding by lazy {
         ActivityAddItemBinding.inflate(layoutInflater)
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference







        binding.SaveButton.setOnClickListener{


            foodName = binding.AddFoodName.text.toString().trim()
            foodPrice = binding.AddFoodPrice.text.toString().trim()
            foodDes = binding.AddItemDes.text.toString().trim()
            foodIngredient = binding.AddItemIngredeint.text.toString().trim()



            if(foodName.isBlank() || foodPrice.isBlank() || foodDes.isBlank() || foodIngredient.isBlank()){
                Toast.makeText(this, "empty fields are not allowed", Toast.LENGTH_SHORT).show()
                binding.AddFoodName.error = "Name cannot be empty"
                binding.AddFoodPrice.error = "Price cannot be empty"
                binding.AddItemDes.error = "Description cannot be empty"
                binding.AddItemIngredeint.error = "Ingredient cannot be empty"
                return@setOnClickListener
            }
            else
            {

                saveData(foodName , foodPrice , foodDes , foodIngredient)
                Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show()
                finish()
            }


        }


        binding.AddItemSelectImage.setOnClickListener{

            pickImage.launch("image/*")

        }



        binding.AddItemBackButton.setOnClickListener{


            startActivity(Intent(this , MainActivity::class.java))

        }
    }

    private fun saveData(foodName: String, foodPrice: String, foodDes: String, foodIngredient: String) {

         val FoodItem= db.database.getReference("Menu")
        //generate a unique key for the food item
        val newItemKey = FoodItem.push().key

        if(image!= null)
        {
            val  storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(image!!)
            uploadTask.addOnSuccessListener {

                imageRef.downloadUrl.addOnSuccessListener { uri ->

                    val allMenu =
                        AllMenu(foodName, foodPrice, foodDes, foodIngredient, uri.toString())


                    newItemKey?.let {
                       key->
                        FoodItem.child(key).setValue(allMenu).addOnSuccessListener {

                            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener{

                                Toast.makeText(this, "Item Failed", Toast.LENGTH_SHORT).show()
                            }

                    }
                }



            }
                .addOnFailureListener{
                    Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show()
        }

    }


   private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent() ) { uri ->
        if (uri != null) {

            image =uri
            binding.AddItemImage.setImageURI(uri)
        }
    }


}