package com.example.upview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CryptoCurrency(
    val name: String,
    val currentValue: String,
    val description: String,
    val yearCreated: Int
) : Parcelable
