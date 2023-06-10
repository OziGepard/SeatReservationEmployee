package com.example.seatreservationemployee.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Issue(
    val id: String,
    val email: String,
    val content: String,
    val title: String
    ) : Parcelable