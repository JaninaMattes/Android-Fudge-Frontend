package com.mobilesystems.feedme.ui.authentication

interface BaseAuthViewModel {

    fun loadLoggedIndUser()

    fun login(email: String, password: String)

    fun register(firstname: String, lastname: String, email: String, password: String, passwordConfirm: String)

    fun logout(username: String, password: String)

    fun observeLoginDataChanged(username: String, password: String)

    fun observeRegisterDataChanged(firstname: String, lastname: String, email: String, password: String, passwordConfirm: String)
}