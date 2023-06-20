package tn.esprit.miniprojectforintegratedprojectsmanagement.models

import com.google.gson.annotations.SerializedName

data class ProfilPassword (

    @SerializedName("password")var email: String,
    @SerializedName("email")var name: String?= null,


)
data class Forgot(
    @SerializedName("email")var email: String
)

