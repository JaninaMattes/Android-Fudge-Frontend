package com.mobilesystems.feedme.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

@Parcelize
data class User(
    val userId: Int,
    val userImage: String? = null,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    var userSettings: Settings? = null,
    val userTags: List<FoodType>? = null
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (userImage != other.userImage) return false
        if (userName != other.userName) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (userSettings != other.userSettings) return false
        if (userTags != other.userTags) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + (userImage?.hashCode() ?: 0)
        result = 31 * result + userName.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + (userSettings?.hashCode() ?: 0)
        result = 31 * result + (userTags?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "User(userId=$userId, userImage=$userImage, userName='$userName', firstName='$firstName', " +
                "lastName='$lastName', email='$email', password='$password', userSettings=$userSettings, userTags=$userTags)"
    }

}