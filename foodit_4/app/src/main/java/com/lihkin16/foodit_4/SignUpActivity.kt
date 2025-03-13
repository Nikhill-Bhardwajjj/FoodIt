package com.lihkin16.foodit_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.lihkin16.foodit_4.databinding.ActivitySignUpBinding
import com.lihkin16.foodit_4.model.UserModel
import kotlin.math.log

class SignUpActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth
    private lateinit var  email : String
    private lateinit var password : String
    private lateinit var userName : String
    private lateinit var restaurantName  : String
    private lateinit var db   : DatabaseReference


    private  val binding : ActivitySignUpBinding by lazy {

        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        //firebase Intalise

        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference


        binding.SignUpButton.setOnClickListener {

            email = binding.SignupEmail.text.toString().trim()
            password = binding.SignupPassword.text.toString().trim()
            userName = binding.SignUpName.text.toString().trim()
            /*restaurantName = binding.SignUpResturant.text.toString().trim()*/

            if(email.isBlank() || password.isBlank() || userName.isBlank()) {

                Toast.makeText(this, "empty fields are not allowed", Toast.LENGTH_SHORT).show()
                binding.SignupEmail.error = "Email cannot be empty"
                binding.SignupPassword.error = "Password cannot be empty"
                binding.SignUpName.error = "Name cannot be empty"
                return@setOnClickListener
            }
            else
            {

                createAccount(email, password, userName)

            }



        }

        binding.haveAccountText.setOnClickListener {
            val intent = android.content.Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String, userName: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        this,
                        "You have  successfully created your account",
                        Toast.LENGTH_SHORT
                    ).show()
                    saveUserData()
                    val intent = android.content.Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                else
                {
                    Toast.makeText(
                        this,
                        "Sign Up Failed",
                        Toast.LENGTH_SHORT
                    ).show()

                    Log.d("account", "createUserWithEmail:failure", task.exception)
                }

                }



    }


    // saving data in database

    private fun saveUserData() {

        email = binding.SignupEmail.text.toString().trim()
        password = binding.SignupPassword.text.toString().trim()
        userName = binding.SignUpName.text.toString().trim()


        val user = UserModel( userName, email, password)
        val UserId = FirebaseAuth.getInstance().currentUser!!.uid


        // save user data base in firebase database

        db.child("admins").child(UserId).setValue(user)








    }
}