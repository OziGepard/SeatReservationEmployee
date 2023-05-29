package com.example.seatreservationemployee.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.seatreservationemployee.repository.EmployeeRepository
import com.example.seatreservationemployee.repository.EmployeeRepositoryImpl
import com.example.seatreservationemployee.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import io.mockk.coEvery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock

class EmployeeViewModelTest {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var repository: EmployeeRepositoryImpl
    private lateinit var viewModel: EmployeeViewModel

    @Before
    fun setUp() {
        mAuth = FirebaseAuth.getInstance()
        repository = EmployeeRepositoryImpl(mAuth, mock(), mock())
        viewModel = EmployeeViewModel(mock(), repository)
    }




    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `When a login-related error occurs`() = runTest {

        viewModel.signInUser("qwe", "asd")
        print(viewModel.userSignUpStatus.value)
    }




}