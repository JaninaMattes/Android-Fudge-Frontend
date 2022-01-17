package com.mobilesystems.feedme.data.response

import android.os.Parcelable
import com.mobilesystems.feedme.domain.model.Settings
import kotlinx.android.parcel.Parcelize

/**
 * Map response jsonObject to UserResponse data class.
 */
@Parcelize
data class UserResponse (

    val userId : Int,
    val firstName : String,
    val lastName : String,
    val email : String,
    val password : String,
    val userSettings : Settings,
    val imgUrl : String,
    val dietaryPreferences : List<String>

) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserResponse

        if (userId != other.userId) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (userSettings != other.userSettings) return false
        if (imgUrl != other.imgUrl) return false
        if (dietaryPreferences != other.dietaryPreferences) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + userSettings.hashCode()
        result = 31 * result + imgUrl.hashCode()
        result = 31 * result + dietaryPreferences.hashCode()
        return result
    }

    override fun toString(): String {
        return "UserResponse(userId=$userId, firstName='$firstName', lastName='$lastName', email='$email', password='$password', userSettings='$userSettings', imgUrl='$imgUrl', dietaryPreferences=$dietaryPreferences)"
    }


}
