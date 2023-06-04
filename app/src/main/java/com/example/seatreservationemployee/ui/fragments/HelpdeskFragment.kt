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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.adapters.IssuesAdapter
import com.example.seatreservationemployee.databinding.FragmentHelpdeskBinding
import com.example.seatreservationemployee.models.Issue
import com.example.seatreservationemployee.ui.EmployeeActivity
import com.example.seatreservationemployee.ui.EmployeeViewModel
import com.example.seatreservationemployee.utils.Resource
import com.google.android.material.snackbar.Snackbar

class HelpdeskFragment : Fragment(R.layout.fragment_helpdesk) {

    private var _binding: FragmentHelpdeskBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HelpdeskFragment"
    lateinit var viewModel: EmployeeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpdeskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel = (activity as EmployeeActivity).viewModel
        initializeUI()
    }

    private fun initializeUI() {
        /**
         * IssueAdapter Initialize
         */

        Log.d("TEST", "initializeUI: Przed stworzeniem")
        val issueAdapter = IssuesAdapter(this::changeFragment)
        binding.issueRecyclerview.apply {
            adapter = issueAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false)
            setHasFixedSize(false)
            registerForContextMenu(this)
        }

        viewModel.receiveIssues()

        viewModel.receiveIssuesStatus.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.issueProgressbar.isVisible = true
                }
                is Resource.Success -> {
                    binding.issueProgressbar.isVisible = false
                    val issueList = mutableListOf<Issue>()
                    var issue: Issue
                    resource.data!!.forEach { document ->
                        issue = Issue(
                            document.get("e-mail").toString(),
                            document.get("content").toString(),
                            document.get("topic").toString()
                        )
                        issueList.add(issue)
                    }
                    issueAdapter.updateList(issueList)
                }
                is Resource.Error -> {
                    binding.issueProgressbar.isVisible = false
                    Snackbar.make(binding.root, resource.message!!, Snackbar.LENGTH_SHORT).show()
                    Log.d(TAG, "initializeUI: ${resource.message}")
                }
            }
        }

    }

    private fun changeFragment(issue: Issue) {
        val action = HelpdeskFragmentDirections.actionHelpdeskFragmentToHelpdeskTicketFragment(issue)
        findNavController().navigate(action)
    }
}