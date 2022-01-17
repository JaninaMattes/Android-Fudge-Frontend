package com.mobilesystems.feedme.ui.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.mobilesystems.feedme.R
import com.mobilesystems.feedme.data.repository.AuthRepositoryImpl
import com.mobilesystems.feedme.ui.authentication.AuthFormState
import com.mobilesystems.feedme.ui.authentication.AuthResult
import com.mobilesystems.feedme.ui.authentication.LoggedInUser
import com.mobilesystems.feedme.ui.common.utils.decodeJWTToken
import com.mobilesystems.feedme.ui.common.utils.saveObjectToSharedPreference
import com.mobilesystems.feedme.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
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
        viewModelScope.launch {
            try {
                val result = registerRepository.register(username, username, email, password)
                if (result.data != null) {
                    // stores less data for displaying
                    val registeredUser = convertTokenToUser(result.data["token"])
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

    private fun convertTokenToUser(jwt: String?): LoggedInUser? {
        var user: LoggedInUser? = null
        if(jwt != null){
            saveObjectToSharedPreference(context, "mPreference", "jwtToken", jwt);
            val decoded = decodeJWTToken(jwt)
            Log.d("Decoded token", decoded)
            val jsonObj = JSONObject(decoded)
            Log.d("Json object", jsonObj.toString())
            user = LoggedInUser(
                userId = jsonObj.get("userId") as Int,
                firstName = jsonObj.get("firstName") as String,
                lastName = jsonObj.get("lastName") as String,
                email = jsonObj.get("email") as String
            )
        }
    return user
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