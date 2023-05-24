package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.databinding.FragmentEmployeeOptionsBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel
import com.example.seatreservationemployee.utils.Resource

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
                    binding.getDateFromAPI.text =
                        resources.getString(R.string.date_from_api_loading)
                }

                is Resource.Success -> {
                    binding.getDateFromAPI.text =
                        resources.getString(R.string.date_from_api_success)
                    viewModel.updateReservations(it.data!!.datetime)
                }

                is Resource.Error -> {
                    binding.getDateFromAPI.text =
                        buildString {
                            append(resources.getString(R.string.date_from_api_error))
                            append("\n")
                            append(it.message)
                        }
                    Log.d(TAG, "initializeUI: ${it.message}")
                }
            }
        }

        viewModel.reservationsUpdateStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.updateReservations.text =
                        resources.getString(R.string.update_reservations_loading)
                }

                is Resource.Success -> {
                    binding.updateReservations.text =
                        resources.getString(R.string.update_reservations_success)
                }

                is Resource.Error -> {
                    binding.updateReservations.text =
                        buildString {
                            append(resources.getString(R.string.update_reservations_error))
                            append("\n")
                            append(it.message)
                        }
                    Log.d(TAG, "initializeUI: ${it.message}")
                }
            }
        }
    }

}