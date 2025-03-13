package com.lihkin16.foodit_3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.lihkin16.foodit_3.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var userName: String
    private lateinit var db: DatabaseReference

    private  val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = Firebase.database.reference

        binding.loginButton.setOnClickListener {

            email = binding.LoginEmail.text.toString().trim()
            password = binding.LoginPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "empty fields are not allowed", Toast.LENGTH_SHORT).show()
                binding.LoginEmail.error = "Email cannot be empty"
                binding.LoginPassword.error = "Password cannot be empty"
                return@setOnClickListener
            } else {

                loginAccount()
            }


        }
        binding.donthaveAccountText.setOnClickListener {
            val intent = android.content.Intent(this,SignUpActivity::class.java)
            startActivity(intent)


        }


    }

    private fun loginAccount() {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                if (task.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    val intent = android.content.Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, "Login Failed try using different email or password", Toast.LENGTH_SHORT).show()
                }
            }
    }
}