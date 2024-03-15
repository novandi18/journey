package com.novandi.journey.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.novandi.core.consts.Disabilities
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JobSeekerRegisterViewModel @Inject constructor(): ViewModel() {
    val disabilities = Disabilities.getDisabilities()

    var name by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    var address by mutableStateOf("")
        private set

    var age by mutableStateOf("")
        private set

    var gender by mutableIntStateOf(-1)
        private set

    var disability by mutableIntStateOf(-1)
        private set

    var password by mutableStateOf("")
        private set

    fun setOnName(value: String) {
        name = value
    }

    fun setOnEmail(value: String) {
        email = value
    }

    fun setOnPhoneNumber(value: String) {
        phoneNumber = value
    }

    fun setOnAddress(value: String) {
        address = value
    }

    fun setOnAge(value: String) {
        age = value
    }

    fun setOnGender(index: Int) {
        gender = index
    }

    fun setOnDisability(index: Int) {
        disability = index
    }

    fun setOnPassword(value: String) {
        password = value
    }

    fun validateFields(): Boolean =
        email.isNotEmpty() && password.isNotEmpty() &&
        phoneNumber.isNotEmpty() && address.isNotEmpty() &&
        age.isNotEmpty() && gender != -1 && disability != -1
}