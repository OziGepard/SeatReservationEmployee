package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.databinding.FragmentLoginBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel
import com.example.seatreservationemployee.utils.Resource
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: EmployeeViewModel
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel = (activity as EmployeeActivity).viewModel

        initializeUI()
    }

    private fun initializeUI() {

        val loginButton = binding.loginButton
        var loginText: String
        var passwordText: String

        viewModel.userSignUpStatus.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    binding.loginProgress.isVisible = true
                }

                is Resource.Success -> {
                    binding.loginProgress.isVisible = false
                    viewModel.retrofitGetDate()
                    val action = LoginFragmentDirections.actionLoginFragmentToEmployeeOptions()
                    findNavController().navigate(action)
                }

                is Resource.Error -> {
                    binding.loginProgress.isVisible = false
                    Log.d(TAG, "initializeUI: ${it.message}")
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        loginButton.setOnClickListener {
            loginText = binding.loginTextinput.text.toString()
            passwordText = binding.passwordTextinput.text.toString()
            viewModel.signInUser(loginText, passwordText)
        }

    }
}