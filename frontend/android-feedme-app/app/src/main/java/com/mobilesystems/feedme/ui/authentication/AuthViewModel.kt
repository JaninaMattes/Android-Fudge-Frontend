package com.mobilesystems.feedme.ui.authentication

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.data.repository.AuthRepositoryImpl
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.common.networkresult.Response
import com.mobilesystems.feedme.ui.common.utils.saveObjectToSharedPreference
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 *
 * Coroutines ViewModel: https://developer.android.com/kotlin/coroutines/coroutines-best-practices
 */

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val androidApplication : Application,
    private val loginRepository: AuthRepositoryImpl) : BaseViewModel(androidApplication), BaseAuthViewModel {

    private val _loginForm = MutableLiveData<AuthFormState>()
    val loginFormState: LiveData<AuthFormState> = _loginForm

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult> = _loginResult

    private val _registerForm = MutableLiveData<AuthFormState>()
    val registerFormState: LiveData<AuthFormState> = _registerForm

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult> = _registerResult

    override fun login(username: String, password: String) {
        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result = loginRepository.login(username, password)

            if (result is Response.Success && result.data != null) {
                // stores less data for displaying
                val loggedInUser = LoggedInUser(
                    userId = result.data.userId,
                    firstName = result.data.firstName,
                    lastName = result.data.lastName)
                _loginResult.value = AuthResult(success = loggedInUser)
                // make logged in user information available
                saveLoggedInUserToSharedPreference(loggedInUser)
            } else {
                _loginResult.value = AuthResult(error = R.string.login_failed)
            }
        }
    }

    override fun observeLoginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = AuthFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = AuthFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = AuthFormState(isDataValid = true)
        }
    }

    override fun register(username: String, email: String, password: String, passwordConfirm: String) {

        viewModelScope.launch {
            // can be launched in a separate asynchronous job
            val result = loginRepository.register(username, email, password, passwordConfirm)

            if (result is Response.Success && result.data != null) {
                // stores less data for displaying
                val registeredUser = LoggedInUser(
                    userId = result.data.userId,
                    firstName = result.data.firstName,
                    lastName = result.data.lastName)
                _registerResult.value = AuthResult(success = registeredUser)
                // make logged in user information available
                saveLoggedInUserToSharedPreference(registeredUser)
            } else {
                _registerResult.value = AuthResult(error = R.string.login_failed)
            }
        }
    }

    override fun observeRegisterDataChanged(username: String, email: String, password: String, passwordConfirm: String) {
        if (!isUserNameValid(username)) {
            _registerForm.value = AuthFormState(usernameError = R.string.invalid_username)
        } else if (!isUserNameValid(email)) {
            _registerForm.value = AuthFormState(emailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = AuthFormState(passwordError = R.string.invalid_password)
        } else if (!isPasswordConfirmed(password, passwordConfirm)){
            _registerForm.value = AuthFormState(confirmPasswordError = R.string.invalid_passwords)
        }else {
            _registerForm.value = AuthFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        if (username.isEmpty()) {
            return false
        }
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            return username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.isNotEmpty() && password.length > 5
    }

    // Check if password equals passwordConfirm
    private fun isPasswordConfirmed(password: String, passwordConfirm: String): Boolean {
        return password == passwordConfirm
    }

    private fun saveLoggedInUserToSharedPreference(user: LoggedInUser){
        val context = getApplication<Application>().applicationContext
        saveObjectToSharedPreference(context,
            "mPreference",
            "loggedInUser", user)
    }
}
