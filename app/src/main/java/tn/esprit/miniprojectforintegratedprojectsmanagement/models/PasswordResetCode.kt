package tn.esprit.miniprojectforintegratedprojectsmanagement.models


import com.google.gson.annotations.SerializedName

data class PasswordResetCode (
    @SerializedName("resetCode") var resetCode: String,
    @SerializedName("userId")var userId: String
        )


