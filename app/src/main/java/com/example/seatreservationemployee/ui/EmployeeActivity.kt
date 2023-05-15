package com.example.seatreservationemployee.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seatreservationemployee.databinding.ActivityEmployeeBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.navigation.NavController

@AndroidEntryPoint
class EmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeBinding
    val viewModel: EmployeeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}