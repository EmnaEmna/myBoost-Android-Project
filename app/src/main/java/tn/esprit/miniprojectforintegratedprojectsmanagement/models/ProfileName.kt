package tn.esprit.miniprojectforintegratedprojectsmanagement.models

import com.google.gson.annotations.SerializedName

data class ProfileName (

    @SerializedName("name")var email: String,
    @SerializedName("email")var name: String?= null,


)
