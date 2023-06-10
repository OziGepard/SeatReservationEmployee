package com.example.seatreservationemployee.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.seatreservationemployee.databinding.FragmentHelpdeskTicketBinding
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel
import com.example.seatreservationemployee.utils.Resource
import com.google.android.material.snackbar.Snackbar

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

        binding.ticketIssueSendButton.setOnClickListener {
            val message = binding.ticketIssueMessage.text.toString()

            viewModel.sendReply(message, args.issue.email)
        }

        viewModel.replyStatus.observe(this) {

            when(it) {
                is Resource.Loading -> {
                    binding.ticketIssueSendButton.isEnabled = false
                    binding.ticketIssueProgressbar.isVisible = true
                }

                is Resource.Error -> {
                    binding.ticketIssueSendButton.isEnabled = true
                    binding.ticketIssueProgressbar.isVisible = false
                    Snackbar.make(binding.root, it.message!!, Snackbar.LENGTH_SHORT).show()

                }

                is Resource.Success -> {
                    binding.ticketIssueSendButton.isEnabled = true
                    binding.ticketIssueProgressbar.isVisible = false
                    Snackbar.make(binding.root, it.data!!, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()

                    viewModel.deleteUserIssue(args.issue.id)
                    viewModel.clearReplyStatus()
                }
                else -> Log.d(TAG, "initializeUI: ReplyStatus Cleared")
            }
        }
    }
}