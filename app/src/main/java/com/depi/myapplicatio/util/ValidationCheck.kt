package com.depi.myapplicatio.util

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
fun validateEmail(email: String): RegisterValidation {
    // Check if email is empty
    if (email.isEmpty()) {
        return RegisterValidation.Failed("Email cannot be empty")
    }

    // Check if the email ends with @gmail.com
    if (!email.endsWith("@gmail.com")) {
        return RegisterValidation.Failed("Email must be a Gmail address (ends with @gmail.com)")
    }

    // Check if the email starts with a letter
    if (!email.first().isLetter()) {
        return RegisterValidation.Failed("Email must start with a letter")
    }
    if (!email.any { it.isDigit() }) {
        return RegisterValidation.Failed("Email must contain at least one digit")
    }

    // Regular expression to validate the format of the email
    val regex = Regex("^[a-zA-Z][a-zA-Z0-9._%+-]*@gmail\\.com$")

    // Validate against the regex
    return if (!regex.matches(email)) {
        RegisterValidation.Failed("Invalid email format. Must only contain letters, digits, and valid special characters before @gmail.com")
    } else {
        RegisterValidation.Success
    }
}

fun validatePassword(password: String , confirmPassword: String): RegisterValidation{
    if(password.isEmpty())
        return RegisterValidation.Failed("password can not be Empty")

    if(confirmPassword.isEmpty())
        return RegisterValidation.Failed(" Confirm Password can not be Empty")

    if(password.length<6)
        return RegisterValidation.Failed("password should contains 6 characters")

    if (password != confirmPassword)
        return RegisterValidation.Failed("Passwords do not match")

    return RegisterValidation.Success

}