package tn.esprit.miniprojectforintegratedprojectsmanagement.models

import com.google.gson.annotations.SerializedName

data class Group(
    @SerializedName("_id") var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("classroom") var classroom: String,
    @SerializedName("year") var year: String,
    @SerializedName("emails") var emails: MutableList<String>?,


    )