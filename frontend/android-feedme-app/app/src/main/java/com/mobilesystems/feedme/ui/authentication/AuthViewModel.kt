package com.mobilesystems.feedme.ui.authentication

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.data.repository.AuthRepositoryImpl
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.data.repository.UserRepositoryImpl
import com.mobilesystems.feedme.domain.model.User
import com.mobilesystems.feedme.ui.common.utils.*
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
    androidApplication : Application,
    private val loginRepository: AuthRepositoryImpl,
    private val userRepository: UserRepositoryImpl
) : BaseViewModel(androidApplication), BaseAuthViewModel {

    // Only expose immutable types
    private val _loginForm = MutableLiveData<AuthFormState>()
    val loginFormState: LiveData<AuthFormState>
        get() = _loginForm

    private val _loginResult = MutableLiveData<AuthResult>()
    val loginResult: LiveData<AuthResult>
        get() = _loginResult

    private val _registerForm = MutableLiveData<AuthFormState>()
    val registerFormState: LiveData<AuthFormState>
        get() = _registerForm

    private val _registerResult = MutableLiveData<AuthResult>()
    val registerResult: LiveData<AuthResult>
        get() = _registerResult

    private var _loggedInUser = MutableLiveData<User?>()
    val loggedInUser : LiveData<User?>
        get() = _loggedInUser

    private var _currentUser = MutableLiveData<LoggedInUser?>()
    val currentUser : LiveData<LoggedInUser?>
        get () = _currentUser

    init{
        val context = getApplication<Application>().applicationContext
        getCurrentUser(context)
        val userId = currentUser.value
        if (userId != null){
            loadLoggedIndUser()
        }
    }

    override fun loadLoggedIndUser() {
        viewModelScope.launch {
            // This is a coroutine scope with the lifecycle of the ViewModel
            val user = currentUser.value
            if(user != null) {
                userRepository.getLoggedInUser(user.userId)
                _loggedInUser = userRepository.currentLoggedInUser
            }
        }
    }

    override fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                // can be launched in a separate asynchronous job
                val result = loginRepository.login(email, password)
                if (result.data != null) {
                    // stores less data for displaying
                    val context = getApplication<Application>().applicationContext
                    val loggedInUser = convertTokenToUser(context, result.data["token"])
                    if (loggedInUser != null) {
                        _loginResult.value = AuthResult(success = loggedInUser)
                        // make logged in user information available
                        saveLoggedInUserToSharedPreference(context, loggedInUser)
                    } else {
                        _loginResult.value = AuthResult(error = R.string.login_failed)
                    }
                } else {
                    _loginResult.value = AuthResult(error = R.string.login_failed)
                }
            }catch (error: Throwable){
                // Notify view login attempt failed
                Log.e("Authentification", "error during login $error")
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

    override fun register(firstname: String, lastname: String, email: String, password: String, passwordConfirm: String) {
        viewModelScope.launch {
            try {
                val result = loginRepository.register(firstname, lastname, email, password)
                if (result.data != null) {
                    // stores less data for displaying
                    val context = getApplication<Application>().applicationContext
                    val registeredUser = convertTokenToUser(context, result.data["token"])
                    if (registeredUser != null){
                        _registerResult.value = AuthResult(success = registeredUser)
                    }else {
                        _registerResult.value = AuthResult(error = R.string.register_failed)
                    }

                } else {
                    _registerResult.value = AuthResult(error = R.string.register_failed)
                }

            } catch (error: Throwable) {
                // Notify view login attempt failed
                Log.e("AuthViewModel", "error during registering $error")
                _registerResult.value = AuthResult(error = R.string.register_failed)
            }
        }
    }

    override fun observeRegisterDataChanged(firstname:String, lastname: String, email: String, password: String, passwordConfirm: String) {
        if (!isUserNameValid(firstname)) {
            _registerForm.value = AuthFormState(usernameError = R.string.invalid_username)
        } else if (!isUserNameValid(lastname)){
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

    override fun logout(username:String, password:String){
        viewModelScope.launch {
            loginRepository.logout(username, password)
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

    private fun getCurrentUser(context: Context): LiveData<LoggedInUser?>{
        val result = getLoggedInUser(context)
        _currentUser.value = result
        return  currentUser
    }
}
