package com.lihkin16.foodit_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.lihkin16.foodit_4.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {

    private val binding: ActivityAdminProfileBinding by lazy {

        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.EditName.isEnabled = false
        binding.EditEmail.isEnabled = false
        binding.EditPhoneNo.isEnabled = false
        binding.EditPassword.isEnabled = false
        binding.EditAddress.isEnabled = false

        var isEnable = false

        binding.EditProfileInfo.setOnClickListener{

            isEnable = !isEnable

            binding.EditName.isEnabled = isEnable
            binding.EditEmail.isEnabled = isEnable
            binding.EditPhoneNo.isEnabled = isEnable
            binding.EditPassword.isEnabled = isEnable
            binding.EditAddress.isEnabled = isEnable






            if(isEnable){
                binding.EditName.requestFocus()

                binding.AdminProfileImage.setOnClickListener{

                    pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }

        }





    }

    val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
        if(uri != null){
            binding.AdminProfileImage.setImageURI(uri)
        }
    }
}