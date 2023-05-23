package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.databinding.FragmentEmployeeOptionsBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel
import com.example.seatreservationemployee.utils.Resource
import com.google.android.material.snackbar.Snackbar

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
        initializeUI()
    }

    private fun initializeUI() {

        viewModel.datetimeFromAPIStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.getDateFromAPI.text = "Pobrano datę z API!"
                }

                is Resource.Success -> {
                    binding.getDateFromAPI.text = "Pobrano datę z API!"
                    viewModel.updateReservations(it.data!!.datetime)
                }

                is Resource.Error -> {
                    binding.getDateFromAPI.text = "Nie udało się pobrać daty z API!"
                    Log.d(TAG, "initializeUI: ${it.message}")
                    Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.reservationsUpdateStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    Snackbar.make(requireView(), "Usuwanie zaległych rezerwacji", Snackbar.LENGTH_SHORT).show()
                }

                is Resource.Success -> {
                    Snackbar.make(requireView(), "Usuwanięto zaległe rezerwacje!", Snackbar.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Log.d(TAG, "initializeUI: ${it.message}")
                    Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}