package com.mobilesystems.feedme.ui.authentication

/**
 * Authentication result : success (user details) or error message.
 */
data class AuthResult(
    val success: LoggedInUser? = null,
    val error: Int? = null
)