package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.databinding.FragmentEmployeeOptionsBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel

class EmployeeOptions : Fragment(R.layout.fragment_employee_options) {

    private var _binding: FragmentEmployeeOptionsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: EmployeeViewModel
    private val TAG = "EmployeeOptions"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeeOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel = (activity as EmployeeActivity).viewModel
    }

}