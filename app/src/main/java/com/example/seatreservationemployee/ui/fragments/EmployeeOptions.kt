package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.databinding.FragmentEmployeeOptionsBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel
import com.example.seatreservationemployee.utils.Resource

class EmployeeOptions : Fragment(R.layout.fragment_employee_options) {

    private var _binding: FragmentEmployeeOptionsBinding? = null
    private val binding get() = _binding!!
    private val TAG = "EmployeeOptions"
    private var isReservationUpdated = false
    lateinit var viewModel: EmployeeViewModel

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

        /**
         * Event Button: Responding to reservations
         */

        binding.helpdeskOption.setOnClickListener {
            val action = EmployeeOptionsDirections.actionEmployeeOptionsToHelpdeskFragment()
            findNavController().navigate(action)
        }

        /**
         * Retrieve date and time from API
         */
        if(!isReservationUpdated) {
            viewModel.datetimeFromAPIStatus.observe(this) {
                Log.d(TAG, "initializeUI: AGAIN!")
                when (it) {
                    is Resource.Loading -> {
                        binding.getDateFromAPI.text =
                            resources.getString(R.string.date_from_api_loading)
                    }

                    is Resource.Success -> {
                        isReservationUpdated = true
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
        }

        /**
         * Updating reservations on the FireBase side
         */

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