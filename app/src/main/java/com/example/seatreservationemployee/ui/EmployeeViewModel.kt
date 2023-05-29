package com.example.seatreservationemployee.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.seatreservationemployee.repository.EmployeeRepositoryImpl
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
    private val repo: EmployeeRepositoryImpl
) : AndroidViewModel(app) {
    private val TAG = "EmployeeViewModel"

    private val context = app.applicationContext

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val _datetimeFromAPIStatus = MutableLiveData<Resource<DateResponse>>()
    val datetimeFromAPIStatus: LiveData<Resource<DateResponse>> = _datetimeFromAPIStatus

    private val _reservationsUpdateStatus = MutableLiveData<Resource<QuerySnapshot>>()
    val reservationsUpdateStatus: LiveData<Resource<QuerySnapshot>> = _reservationsUpdateStatus

    private val _receiveIssuesStatus = MutableLiveData<Resource<QuerySnapshot>>()
    val receiveIssuesStatus: LiveData<Resource<QuerySnapshot>> = _receiveIssuesStatus

    fun receiveIssues() {
        if (!isOnline()) {
            _receiveIssuesStatus.postValue(Resource.Error("Brak połączenia z Internetem!"))
        } else {
            _receiveIssuesStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val receiveIssues = repo.receiveIssues()
                _receiveIssuesStatus.postValue(receiveIssues)
            }
        }
    }

    fun signInUser(userLogin: String, userPassword: String) {
        if (userLogin.isEmpty() || userPassword.isEmpty()) {
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

    private fun isOnline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}