package tn.esprit.miniprojectforintegratedprojectsmanagement.utils

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.*


interface ApiInterface {


    @POST("api/users/login")
    fun signin(@Body userin: User): Call<User>

    @POST("/api/users/register")
    fun signup(@Body NewUser: User): Call<User>

    @POST("/api/group/creategroup")
    fun addgroup(@Body newGroup: Group): Call<Group>

    @POST("/api/userp/resetPwd")
    fun resetPassword(@Body NewProfilPassword: ProfilPassword): Call<ProfilPassword>

    @POST("api/userp/editus")
    fun editName(@Body NewProfileName: ProfileName): Call<ProfileName>

    @GET("/api/group/getgroups")
    fun getgroups(): Call<List<Group>>

    @POST("/api/project/getProject")
    fun getProjects(@Body groupdId: GroupId): Call<List<Project>>


    @POST("/api/project/createproject")
    fun createproject(@Body NewProject: Project): Call<Project>
    @POST("/api/userp/forgot_password")
    fun forgot_password(@Body email: Forgot):Call<Forgot>

    @GET("api/project/getEmails")
    fun getEmails(@Query("groupId") groupId: String): Call<MutableList<String>>

    @PUT("api/project/updateProject")
    fun updateProject(@Query("projectId") projectId: String, @Body proj: Project): Call<Project>


    @GET("google")
    fun makeGoogleRequest(): Call<ResponseBody>

    @GET("schedule_event")
    fun makeScheduleEventRequest(): Call<ResponseBody>

    @POST("api/users/forgotpassword")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<ApiResponse>

    @POST("api/users/resetpassword")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): Call<ResetPasswordResponse>

    // Add the updated endpoint for initiating Google Auth
//    @GET("initiateGoogleAuth")
//    fun initiateGoogleAuth(): Call<AuthResponse>
//
//    @POST("exchangeCodeForToken")
//    @FormUrlEncoded
//    fun exchangeCodeForToken(@Field("code") code: String): Call<Void>
//
//    @POST("scheduleEvent")
//    fun scheduleEvent(): Call<Void>
//    data class AuthResponse(val authUrl: String)

    /*companion object {

        var BASE_URL = "http://192.168.1.202:9090/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }

    }*/

}