package com.mobilesystems.feedme.ui.authentication

data class LoggedInUser(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String)