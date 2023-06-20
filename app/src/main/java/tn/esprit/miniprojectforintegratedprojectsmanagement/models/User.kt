package tn.esprit.miniprojectforintegratedprojectsmanagement.models

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("email")var email: String,
    @SerializedName("password")var password: String,
    @SerializedName("token")var token: String?=null,
    @SerializedName("name")var name: String?= null,
    @SerializedName("role")var role: String?= null,



    )
