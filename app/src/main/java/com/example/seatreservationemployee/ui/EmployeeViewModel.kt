package com.example.seatreservationemployee.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.nedim.maildroidx.MaildroidXType
import co.nedim.maildroidx.callback
import co.nedim.maildroidx.sendEmail
import com.example.seatreservationemployee.R
import com.example.seatreservationemployee.repository.EmployeeRepositoryImpl
import com.example.seatreservationemployee.retrofit.DateResponse
import com.example.seatreservationemployee.utils.Resource
import com.example.seatreservationemployee.utils.SecretConstants
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }
    fun getContext(): Context {
        return context
    }
}

@HiltViewModel
class EmployeeViewModel @Inject constructor(
    app: Application,
    private val repo: EmployeeRepositoryImpl,
    private val resourcesProvider: ResourcesProvider
) : AndroidViewModel(app) {
    private val TAG = "EmployeeViewModel"

    private val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
    val userSignUpStatus: LiveData<Resource<AuthResult>> = _userSignUpStatus

    private val _datetimeFromAPIStatus = MutableLiveData<Resource<DateResponse>>()
    val datetimeFromAPIStatus: LiveData<Resource<DateResponse>> = _datetimeFromAPIStatus

    private val _reservationsUpdateStatus = MutableLiveData<Resource<QuerySnapshot>>()
    val reservationsUpdateStatus: LiveData<Resource<QuerySnapshot>> = _reservationsUpdateStatus

    private val _receiveIssuesStatus = MutableLiveData<Resource<QuerySnapshot>>()
    val receiveIssuesStatus: LiveData<Resource<QuerySnapshot>> = _receiveIssuesStatus

    private val _replyStatus = MutableLiveData<Resource<String>?>()
    val replyStatus: LiveData<Resource<String>?> = _replyStatus


    fun signInUser(userLogin: String, userPassword: String) {
        if (userLogin.isEmpty() || userPassword.isEmpty()) {
            _userSignUpStatus.postValue(
                Resource.Error(
                    resourcesProvider.getString(R.string.empty_string)
                )
            )
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

    fun receiveIssues() {
        if (!isOnline()) {
            _receiveIssuesStatus.postValue(
                Resource.Error(
                    resourcesProvider.getString(
                        R.string.no_internet_connection
                    )
                )
            )
        } else {
            _receiveIssuesStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val receiveIssues = repo.receiveIssues()
                _receiveIssuesStatus.postValue(receiveIssues)
            }
        }
    }

    fun isOnline(): Boolean {
        val connectivityManager =
            resourcesProvider
                .getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    fun sendReply(message: String, email: String) {
        if (!isOnline()) {
            _replyStatus.postValue(
                Resource.Error(
                    resourcesProvider.getString(
                        R.string.no_internet_connection
                    )
                )
            )
        } else {
            _replyStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                sendEmail {
                    smtp("smtp.gmail.com")
                    smtpUsername(SecretConstants.EMAIL_USERNAME)
                    smtpPassword(SecretConstants.EMAIL_PASSWORD)
                    port("465")
                    type(MaildroidXType.HTML)
                    to(email)
                    from("janedoen@email.com")
                    subject("Odpowiedź na zapytanie")
                    body(
                        message
                    )
                    callback {
                        timeOut(3000)
                        onSuccess {
                            _replyStatus.postValue(
                                Resource.Success(
                                    resourcesProvider.getString(
                                        R.string.mail_sent_succes
                                    )
                                )
                            )
                        }
                        onFail {
                            _replyStatus.postValue(
                                Resource.Error(
                                    resourcesProvider.getString(
                                        R.string.mail_sent_failure
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }

    }

    fun deleteUserIssue(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val deleteUserIssueStatus = repo.deleteUserIssue(id)
            if (deleteUserIssueStatus is Resource.Error) {
                Log.d(
                    TAG,
                    "deleteUserIssue: Something went wrong, trying again to delete the user issue from FB"
                )
                deleteUserIssue(id)
            }

        }
    }

    fun clearReplyStatus() {
        _replyStatus.postValue(null)
    }
}