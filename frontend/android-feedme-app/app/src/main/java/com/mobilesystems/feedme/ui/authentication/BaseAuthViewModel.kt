package com.mobilesystems.feedme.ui.authentication

interface BaseAuthViewModel {

    fun login(username: String, password: String)

    fun register(username: String, email: String, password: String, passwordConfirm: String)

    fun observeLoginDataChanged(username: String, password: String)

    fun observeRegisterDataChanged(username: String, email: String, password: String, passwordConfirm: String)
}