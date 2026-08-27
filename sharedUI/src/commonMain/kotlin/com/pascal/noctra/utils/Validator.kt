package com.pascal.noctra.utils

object Validator {
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && "@" in email
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
