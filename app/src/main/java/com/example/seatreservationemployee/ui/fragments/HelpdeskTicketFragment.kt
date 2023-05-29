package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.databinding.FragmentHelpdeskBinding
import com.example.seatreservationemployee.databinding.FragmentHelpdeskTicketBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel

class HelpdeskTicketFragment : Fragment() {

    private var _binding: FragmentHelpdeskTicketBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HelpdeskTicketFragment"
    private val args: HelpdeskTicketFragmentArgs by navArgs()
    lateinit var viewModel: EmployeeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpdeskTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel = (activity as EmployeeActivity).viewModel
        initializeUI()
    }

    private fun initializeUI() {

        binding.ticketIssueEmail.setText(args.issue.email)
        binding.ticketIssueTitle.setText(args.issue.title)
        binding.ticketIssueContent.setText(args.issue.content)
    }
}