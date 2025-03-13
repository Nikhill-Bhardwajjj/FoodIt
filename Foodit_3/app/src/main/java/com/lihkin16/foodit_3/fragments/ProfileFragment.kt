package com.lihkin16.foodit_3.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.lihkin16.foodit_3.databinding.FragmentProfileBinding
import com.lihkin16.foodit_3.model.UserModel
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: FragmentProfileBinding
    private     var image: Uri?= null
    private lateinit var imageuri :Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        auth = FirebaseAuth.getInstance()


        binding = FragmentProfileBinding.inflate(inflater, container, false)


        setUserData()






        binding.EditName.isEnabled = false
        binding.EditEmail.isEnabled = false
        binding.EditPhoneNo.isEnabled = false

        binding.EditAddress.isEnabled = false

        var isEnable = false

        binding.EditProfileText.setOnClickListener{

            isEnable = !isEnable

            binding.EditName.isEnabled = isEnable
            binding.EditEmail.isEnabled = isEnable
            binding.EditPhoneNo.isEnabled = isEnable

            binding.EditAddress.isEnabled = isEnable






            if(isEnable){
                binding.EditName.requestFocus()

                binding.UserProfileImage.setOnClickListener{

                    pickImage.launch("image/*")
                }
            }

        }





       binding.SaveInfoButton.setOnClickListener{

           val name = binding.EditName.text.toString()
           val email = binding.EditEmail.text.toString()
           val phoneNo = binding.EditPhoneNo.text.toString()
           val address = binding.EditAddress.text.toString()
           
           updateUserData(name,email,phoneNo,address)

       }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, phoneNo: String, address: String) {
        val userId = auth.currentUser?.uid ?: ""
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("users").child(userId)

        if (image != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/${auth.currentUser?.uid}.jpg")
            val uploadTask = imageRef.putFile(image!!)

            uploadTask.addOnSuccessListener {
                // Image upload success, get download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Image download URL obtained, update user data with image URI
                    val imageuri = uri.toString()
                    val userData = hashMapOf<String, Any>(
                        "name" to name,
                        "email" to email,
                        "phoneNo" to phoneNo,
                        "address" to address,
                        "profileImage" to imageuri
                    )

                    reference.updateChildren(userData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show()
                        }
                }
            }
                .addOnFailureListener {
                    // Image upload failure
                    Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        } else {
            // No image selected, update user data without image URI
            val userData = hashMapOf<String, Any>(
                "name" to name,
                "email" to email,
                "phoneNo" to phoneNo,
                "address" to address
            )

            reference.updateChildren(userData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun setUserData() {

        val userId = auth.currentUser?.uid?:""
        if(userId != null)
        {
            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("users").child(userId)

            reference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                   if(snapshot.exists())
                   {
                       val userProfile = snapshot.getValue(UserModel::class.java)
                       if(userProfile != null){
                           binding.EditName.setText(userProfile.name).toString()
                           binding.EditEmail.setText(userProfile.email).toString()
                           binding.EditPhoneNo.setText(userProfile.phoneNo).toString()
                           binding.EditAddress.setText(userProfile.address).toString()
                           Picasso.get().load(userProfile.profileImage).into(binding.UserProfileImage)

                       }
                   }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })





        }


    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent() ) { uri ->
        if (uri != null) {

            image =uri
            binding.UserProfileImage.setImageURI(uri)
        }
    }

    companion object {

    }
}