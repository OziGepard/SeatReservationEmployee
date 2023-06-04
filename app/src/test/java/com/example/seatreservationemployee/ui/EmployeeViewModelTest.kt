@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.seatreservationemployee.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.seatreservationemployee.repository.EmployeeRepositoryImpl
import com.example.seatreservationemployee.retrofit.DateResponse
import com.example.seatreservationemployee.utils.DispatchesProvider
import com.example.seatreservationemployee.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class EmployeeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private val getApplication: Application = mockk(relaxed = true)
    private val getEmployeeRepository: EmployeeRepositoryImpl = mockk()
    private val authResult: AuthResult = mockk()
    private val dateResult: DateResponse = mockk()
    private val queryResult: QuerySnapshot = mockk()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when the user enters the correct login details`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.employeeAuth(any(), any()) } coAnswers {
                    Resource.Success(authResult)
                }
            },
            whenAction = { signInUser("login", "password")},
            thenCheck = { signupStatuses, _, _, _ ->
                Assert.assertTrue(signupStatuses.size == 2)
                Assert.assertTrue(signupStatuses[1] is Resource.Success)
            }
        ).test(this)
    }

    @Test
    fun `when the user enters the incorrect login details`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.employeeAuth(any(), any()) } coAnswers {
                    Resource.Error("Ops")
                }
            },
            whenAction = { signInUser("wrongLogin", "wrongPassword")},
            thenCheck = { signupStatuses, _, _, _ ->
                Assert.assertTrue(signupStatuses.size == 2)
                Assert.assertTrue(signupStatuses[1] is Resource.Error)
            }
        ).test(this)
    }

    @Test
    fun `when you manage to correctly retrieve the date from the API`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.retrofitGetDate() } coAnswers {
                    Resource.Success(dateResult)
                }
            },
            whenAction = { retrofitGetDate()},
            thenCheck = { _, dateTimeStatuses, _, _ ->
                Assert.assertTrue(dateTimeStatuses.size == 2)
                Assert.assertTrue(dateTimeStatuses[1] is Resource.Success)
            }
        ).test(this)
    }

    @Test
    fun `when you manage to incorrectly retrieve the date from the API`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.retrofitGetDate() } coAnswers {
                    Resource.Error("Ops")
                }
            },
            whenAction = { retrofitGetDate()},
            thenCheck = { _, dateTimeStatuses, _, _ ->
                Assert.assertTrue(dateTimeStatuses.size == 2)
                Assert.assertTrue(dateTimeStatuses[1] is Resource.Error)
            }
        ).test(this)
    }

    @Test
    fun `when we're waiting for retrieve a date from the API`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.retrofitGetDate() } coAnswers {
                    Resource.Error("Ops")
                }
            },
            whenAction = { retrofitGetDate()},
            thenCheck = { _, dateTimeStatuses, _, _ ->
                Assert.assertTrue(dateTimeStatuses.size == 2)
                Assert.assertTrue(dateTimeStatuses[0] is Resource.Loading)
            }
        ).test(this)
    }

    @Test
    fun `when we have successfully updated the reservations in FireBase`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.updateReservations(any()) } coAnswers {
                    Resource.Success(queryResult)
                }
            },
            whenAction = { updateReservations("2023-05-05")},
            thenCheck = { _, _, reservationsStatuses, _ ->
                Assert.assertTrue(reservationsStatuses.size == 2)
                Assert.assertTrue(reservationsStatuses[1] is Resource.Success)
            }
        ).test(this)
    }

    @Test
    fun `when the FireBase reservation failed to update`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.updateReservations(any()) } coAnswers {
                    Resource.Error("Ops")
                }
            },
            whenAction = { updateReservations("2023-05-05")},
            thenCheck = { _, _, reservationsStatuses, _ ->
                Assert.assertTrue(reservationsStatuses.size == 2)
                Assert.assertTrue(reservationsStatuses[1] is Resource.Error)
            }
        ).test(this)
    }

    @Test
    fun `when the application detected no internet`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { createViewModel().isOnline() } coAnswers {
                    false
                }
            },
            whenAction = { isOnline()},
            thenCheck = { _, _, _, _ ->
                Assert.assertTrue(!createViewModel().isOnline())
            }
        ).test(this)
    }

    @Test
    fun `when the application detected internet access`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { createViewModel().isOnline() } coAnswers {
                    true
                }
            },
            whenAction = { isOnline()},
            thenCheck = { _, _, _, _ ->
                Assert.assertTrue(createViewModel().isOnline())
            }
        ).test(this)
    }

    @Test
    fun `when successfully retrieved complaints from FireBase`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.receiveIssues() } coAnswers {
                    Resource.Success(queryResult)
                }
                coEvery { createViewModel().isOnline() } coAnswers {
                    true
                }
            },
            whenAction = { receiveIssues()},
            thenCheck = { _, _, _, issuesStatuses ->
                Assert.assertTrue(issuesStatuses.size == 2)
                Assert.assertTrue(issuesStatuses[1] is Resource.Success)
            }
        ).test(this)
    }

    @Test
    fun `when the complaint failed to download from FireBase`() = runTest {
        EmployeeViewModelTestHelper(
            underTestFactory = { createViewModel() },
            givenSetup = {
                coEvery { getEmployeeRepository.receiveIssues() } coAnswers {
                    Resource.Error("Ops")
                }
                coEvery { createViewModel().isOnline() } coAnswers {
                    true
                }
            },
            whenAction = { receiveIssues()},
            thenCheck = { _, _, _, issuesStatuses ->
                Assert.assertTrue(issuesStatuses.size == 2)
                Assert.assertTrue(issuesStatuses[1] is Resource.Error)
            }
        ).test(this)
    }

    private fun createViewModel(): EmployeeViewModel = EmployeeViewModel(
        getApplication,
        getEmployeeRepository
    )
}