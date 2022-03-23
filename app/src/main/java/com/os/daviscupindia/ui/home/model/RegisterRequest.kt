package com.os.daviscupindia.ui.home.model

data class RegisterRequest(
    val email: String,
    val mobile_number: String,
    val name: String,
    val password: String
)

data class VerifyRequest(
    val email: String,
    val otp: String,
)

data class ResendOTPRequest(val email: String)

data class CustomerLoginRequest(
    val email: String? = "", val password: String? = null , val otp: String? = null,
)

data class ForgotPassword(
    val email: String
)