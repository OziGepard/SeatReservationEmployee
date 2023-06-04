@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.seatreservationemployee.ui

import com.example.seatreservationemployee.retrofit.DateResponse
import com.example.seatreservationemployee.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

class EmployeeViewModelTestHelper(
    private val underTestFactory: () -> EmployeeViewModel,
    private val givenSetup: () -> Unit = {},
    private val whenAction: (EmployeeViewModel.() -> Unit)? = null,
    private val thenCheck: (
        List<Resource<AuthResult>>,
        List<Resource<DateResponse>>,
        List<Resource<QuerySnapshot>>,
        List<Resource<QuerySnapshot>>
    ) -> Unit
) {

    fun test(scope: TestScope) {
        givenSetup()

        val underTest = underTestFactory()

        val emittedUserSignUpStatuses = mutableListOf<Resource<AuthResult>>()
        val emittedDateTimeFromAPIStatuses = mutableListOf<Resource<DateResponse>>()
        val emittedReservationsUpdateStatuses = mutableListOf<Resource<QuerySnapshot>>()
        val emittedReceiveIssuesStatuses = mutableListOf<Resource<QuerySnapshot>>()

        val signupStatusJob = scope.launch {
            underTest.userSignUpStatus.observeForever {
                emittedUserSignUpStatuses.add(it)
            }
        }

        val dateTimeStatusJob = scope.launch {
            underTest.datetimeFromAPIStatus.observeForever {
                emittedDateTimeFromAPIStatuses.add(it)
            }
        }

        val reservationsStatusJob = scope.launch {
            underTest.reservationsUpdateStatus.observeForever {
                emittedReservationsUpdateStatuses.add(it)
            }
        }

        val receiveIssuesStatusJob = scope.launch {
            underTest.receiveIssuesStatus.observeForever {
                emittedReceiveIssuesStatuses.add(it)
            }
        }

        scope.advanceUntilIdle()

        whenAction?.invoke(underTest)

        scope.advanceUntilIdle()

        signupStatusJob.cancel()
        dateTimeStatusJob.cancel()
        reservationsStatusJob.cancel()
        receiveIssuesStatusJob.cancel()

        thenCheck(
            emittedUserSignUpStatuses,
            emittedDateTimeFromAPIStatuses,
            emittedReservationsUpdateStatuses,
            emittedReceiveIssuesStatuses
        )
    }

}