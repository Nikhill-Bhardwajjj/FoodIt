package com.lihkin16.foodit_3

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lihkin16.foodit_3.databinding.FragmentCongratsBottomSheetBinding


class CongratsBottomSheet : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentCongratsBottomSheetBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCongratsBottomSheetBinding.inflate(layoutInflater , container , false)

        binding.CongratsGoHomeButton.setOnClickListener {


            startActivity(Intent(context , MainActivity::class.java))

        }
        return binding.root
    }

    companion object {

    }
}