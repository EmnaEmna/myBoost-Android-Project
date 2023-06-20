package tn.esprit.miniprojectforintegratedprojectsmanagement.models

data class ResetPasswordRequest(
    val email: String,
    val verificationCode: String,
    val newPassword: String
)