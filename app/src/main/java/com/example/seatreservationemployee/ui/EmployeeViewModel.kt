package com.example.seatreservationemployee.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seatreservationemployee.repository.EmployeeRepository
import com.example.seatreservationemployee.retrofit.DateResponse
import com.example.seatreservationemployee.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    app: Application,
    private val repo: EmployeeRepository
) : AndroidViewModel(app) {
    private val TAG = "EmployeeViewModel"

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val _datetimeFromAPIStatus = MutableLiveData<Resource<DateResponse>>()
    val datetimeFromAPIStatus: LiveData<Resource<DateResponse>> = _datetimeFromAPIStatus

    private val _reservationsUpdateStatus = MutableLiveData<Resource<QuerySnapshot>>()
    val reservationsUpdateStatus: LiveData<Resource<QuerySnapshot>> = _reservationsUpdateStatus


    fun signInUser(userLogin: String, userPassword: String) {
        if (userLogin.isEmpty() || userPassword.isEmpty()) {
            Log.d(TAG, "signInUser: $userLogin, $userPassword")
            _userSignUpStatus.postValue(Resource.Error("Empty String"))
        } else {
            _userSignUpStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = repo.employeeAuth(userLogin, userPassword)
                _userSignUpStatus.postValue(loginResult)
            }
        }
    }

    fun retrofitGetDate() {
        _datetimeFromAPIStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val datetimeResult = repo.retrofitGetDate()
            _datetimeFromAPIStatus.postValue(datetimeResult)
        }
    }

    fun updateReservations(date: String) {
        val actualDate = LocalDate.parse(date.substring(0, 10))

        _reservationsUpdateStatus.postValue(Resource.Loading())
        viewModelScope.launch(Dispatchers.Main) {
            val reservationsUpdate = repo.updateReservations(actualDate)
            _reservationsUpdateStatus.postValue(reservationsUpdate)
        }
    }
}