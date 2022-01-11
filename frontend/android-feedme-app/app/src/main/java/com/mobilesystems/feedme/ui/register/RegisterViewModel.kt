package com.mobilesystems.feedme.ui.register

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.data.repository.AuthRepositoryImpl
import com.mobilesystems.feedme.ui.authentication.AuthFormState
import com.mobilesystems.feedme.ui.authentication.AuthResult
import com.mobilesystems.feedme.ui.authentication.LoggedInUser
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val androidApplication: Application,
    private val registerRepository: AuthRepositoryImpl) :
    BaseViewModel(androidApplication), BaseRegisterViewModel {

    private val _registerForm = MutableLiveData<AuthFormState>()
    val registerFormState: LiveData<AuthFormState> = _registerForm

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult> = _registerResult

    override fun register(username: String, email: String, password: String, passwordConfirm: String) {
        // can be launched in a separate asynchronous job
        // This is a coroutine scope with the lifecycle of the ViewModel
        viewModelScope.launch {
            val result = registerRepository.register(username, email, password, passwordConfirm)

            if (result is Response.Success && result.data != null) {
                val registeredUser = LoggedInUser(
                    userId = result.data.userId,
                    firstName = result.data.firstName,
                    lastName = result.data.lastName
                )
                _registerResult.value = AuthResult(success = registeredUser)
            } else {
                _registerResult.value = AuthResult(error = R.string.login_failed)
            }
        }
    }

    override fun registerDataChanged(username: String, email: String, password: String, passwordConfirm: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = AuthFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = AuthFormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordConfirmed(password, passwordConfirm)){
            _registerForm.value = AuthFormState(passwordError = R.string.invalid_passwords)
        }else {
            _registerForm.value = AuthFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isPasswordConfirmed(password: String, passwordConfirm: String): Boolean {
        return password == passwordConfirm
    }

}