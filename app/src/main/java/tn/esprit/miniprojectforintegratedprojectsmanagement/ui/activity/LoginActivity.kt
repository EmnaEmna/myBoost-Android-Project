package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity.SigninActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.*
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private var btntosignin: Button? = null
    private var btnlogin: Button? = null
    private var txtLayoutFullName: TextInputLayout? = null
    private lateinit var txtLayoutPassword: TextInputLayout
    private var txtEmail: TextInputEditText? = null
    private var txtPassword: TextInputEditText? = null
    private var btnforgot: Button? = null



    lateinit var PREF_USERNAME: SharedPreferences
    lateinit var PREF_EMAIL: SharedPreferences

    @Inject
    lateinit var services:ApiInterface

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var forgotButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar: Toolbar = findViewById(R.id.app_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Welcome"

        PREF_USERNAME=getSharedPreferences("PREF_USERNAME", Context.MODE_PRIVATE)
        PREF_EMAIL=getSharedPreferences("PREF_EMAIL", Context.MODE_PRIVATE)

        txtLayoutFullName = findViewById(R.id.txtLayoutFullName)
        txtLayoutPassword = findViewById(R.id.txtLayoutPassword)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        btntosignin = findViewById(R.id.btntosignin)
        btnlogin = findViewById(R.id.btnlogin)
        val emailEditText = findViewById<EditText>(R.id.emailcode)

        // Get the token from shared preferences
        val token = sharedPrefs.getToken()
        if (token != null) {

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the login activity to prevent going back to it


        }else {
        btntosignin!!.setOnClickListener {
            clickSignIn()
        }
        btnlogin!!.setOnClickListener {
            clickLogIn()
        }


        }


        forgotButton = findViewById(R.id.btnforgot)
        forgotButton.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun showForgotPasswordDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_password_dialog)
        dialog.setCancelable(false)

        val sendCodeButton = dialog.findViewById<Button>(R.id.btnsendmailcode)
        val cancelButton = dialog.findViewById<Button>(R.id.btncancel)
        val emailEditText = dialog.findViewById<EditText>(R.id.emailcode)

        sendCodeButton.setOnClickListener {
            val email = emailEditText.text.toString()

            // Call the backend function to handle the forgot password request with the email input
            sendForgotPasswordCode(email)
            dialog.dismiss()

            // Open the password enter code dialog
            showPasswordEnterCodeDialog()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun showPasswordEnterCodeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.password_enter_code, null)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
            .setTitle("Enter Verification Code")
            .setPositiveButton("Reset Password") { dialog, _ ->
                val verificationCode = retrieveVerificationCodeFromEditTexts(dialogView)
                val newPassword = retrieveNewPasswordFromEditText(dialogView)
                val email = retrieveEmailFromEditText(dialogView)


                val resetPasswordRequest = ResetPasswordRequest(
                    email = email,
                    verificationCode = verificationCode,
                    newPassword = newPassword
                )
                // Call the backend function to reset the password
                services.resetPassword(resetPasswordRequest).enqueue(object : Callback<ResetPasswordResponse> {

                    override fun onResponse(call: Call<ResetPasswordResponse>, response: Response<ResetPasswordResponse>) {
                        if (response.isSuccessful) {
                            val resetPasswordResponse = response.body()
                            // Handle the successful response
                            Toast.makeText(this@LoginActivity, "Password reset successful", Toast.LENGTH_SHORT).show()

                        } else {
                            // Handle the unsuccessful response
                            Toast.makeText(this@LoginActivity, "Password reset failed", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<ResetPasswordResponse>, t: Throwable) {
                        // Handle the network call failure
                    }
                })
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun retrieveVerificationCodeFromEditTexts(dialogView: View): String {
        val code1 = dialogView.findViewById<EditText>(R.id.editTextCode1).text.toString()
        val code2 = dialogView.findViewById<EditText>(R.id.editTextCode2).text.toString()
        val code3 = dialogView.findViewById<EditText>(R.id.editTextCode3).text.toString()
        val code4 = dialogView.findViewById<EditText>(R.id.editTextCode4).text.toString()
        val code5 = dialogView.findViewById<EditText>(R.id.editTextCode5).text.toString()
        val code6 = dialogView.findViewById<EditText>(R.id.editTextCode6).text.toString()

        // Concatenate the codes
        val verificationCode ="$code1$code2$code3$code4$code5$code6"
        // Handle automatic movement of focus
        if (code1.length == 1) {
            dialogView.findViewById<EditText>(R.id.editTextCode2).requestFocus()
        } else if (code2.length == 1) {
            dialogView.findViewById<EditText>(R.id.editTextCode3).requestFocus()
        } else if (code3.length == 1) {
            dialogView.findViewById<EditText>(R.id.editTextCode4).requestFocus()
        } else if (code4.length == 1) {
            dialogView.findViewById<EditText>(R.id.editTextCode5).requestFocus()
        } else if (code5.length == 1) {
            dialogView.findViewById<EditText>(R.id.editTextCode6).requestFocus()
        }
        return verificationCode

    }

    private fun retrieveNewPasswordFromEditText(dialogView: View): String {
        return dialogView.findViewById<EditText>(R.id.editTextNewPassword).text.toString()
    }
    private fun retrieveEmailFromEditText(dialogView: View): String {
        return dialogView.findViewById<EditText>(R.id.editTextEmail).text.toString()
    }


    private fun sendForgotPasswordCode(email: String) {
        val request = ForgotPasswordRequest(email)
        services.forgotPassword(request).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    // Handle the successful response
                    val apiResponse = response.body()
                    Toast.makeText(this@LoginActivity, apiResponse?.message, Toast.LENGTH_SHORT).show()
                } else {
                    // Handle the error response
                    Toast.makeText(this@LoginActivity, "Error occurred", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // Handle the failure case
                Toast.makeText(this@LoginActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }


//    private fun showCustomDialogBox( email: String? = null) {
//        val dialog  = Dialog(this)
//        val inflater = LayoutInflater.from(this)
//        val dialogView = inflater.inflate(R.layout.layout_password_dialog, null)
//        dialog.setContentView(dialogView)
//        val emailcode = dialogView.findViewById<EditText>(R.id.emailcode)
//
//        // dialog.requestWindowFeature(window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.layout_password_dialog)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
///* val btnsendmailcode = dialogView.findViewById<Button>(R.id.btnsendmailcode)
//    val btncancel = dialogView.findViewById<Button>(R.id.btncancel)*/
//        val btnsendmailcode : Button=dialog.findViewById((R.id.btnsendmailcode))
//        val btncancel : Button=dialog.findViewById((R.id.btncancel))
//
//        emailcode.setText(email) // Set the email value if provided
//
//        btnsendmailcode.setOnClickListener {
//
//            val email = emailcode!!.text.toString()
//            val message = "Email: $email"
//            Log.d("Email", message)
//
//            val request = ForgotPasswordRequest(email)
//
//            services.forgotPassword(request).enqueue(object : Callback<ApiResponse> {
//                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
//                    if (response.isSuccessful) {
//                        val message = response.body()?.message
//                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@LoginActivity, "Failed to send email", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
//                    Toast.makeText(this@LoginActivity, "Failed to send email", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//
//        btncancel.setOnClickListener{
//            dialog.dismiss()
//        }
//        dialog.show()
//    }

    private fun showResetPsw() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.fragment_reset_password,null)
        val editText =dialogLayout.findViewById<EditText>(R.id.Resetemail)
        with(builder){
            setPositiveButton("OK"){dialog, which ->
                var forgot = Forgot(editText.text.toString().trim())
                services.forgot_password(forgot).enqueue(
                    object : Callback<Forgot>{
                        override fun onResponse(call: Call<Forgot>, response: Response<Forgot>) {
                            Toast.makeText(this@LoginActivity, "password send to your email", Toast.LENGTH_SHORT).show()
                        }
                        override fun onFailure(call: Call<Forgot>, t: Throwable) { Toast.makeText(this@LoginActivity, "Error when reset password", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
            setNegativeButton("Cancel"){dialog, which ->
                Log.d("Reset","Password reset cancelled")
            }
            setView(dialogLayout)
            show()
        }
    }

    private fun clickLogIn() {
        if (inputcheck()) {
            val userin = User(txtEmail!!.text.toString(),txtPassword!!.text.toString())
            services.signin(userin).enqueue(
                object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        val userdata = response.body()
                        if (userdata != null){
                            sharedPrefs.saveToken(userdata.token!!)
                            println("here " + userdata.token)
                            if (userdata.role != null) {
                                sharedPrefs.saveUserRole(userdata.role!!)
                            } else {
                                // Gérer le cas où le rôle est nul
                            }


                                sharedPrefs.saveUserEmail(userdata.email)

                        //    sharedPrefs.saveLoggedInStatus(true) // Save login status

                            PREF_USERNAME.edit().putString("PREF_USERNAME",userdata.name).apply()
                            PREF_EMAIL.edit().putString("PREF_EMAIL",userdata.email).apply()

                            println("here username " + userdata.name)
                           ////// Toast.makeText(applicationContext, userdata.token, Toast.LENGTH_SHORT).show()

                            // Check if the user is already logged in

                            val token = sharedPrefs.getToken()

                            if (token!= null) {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Close the login activity to prevent going back to it
                            } else {
                                Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials and try again.", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials and try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@LoginActivity,t.toString(), Toast.LENGTH_LONG).show()
                    }

                }
            )
        }
    }



    private fun inputcheck(): Boolean {
        txtLayoutFullName!!.error = null
        txtLayoutPassword.error = null
        if (txtEmail!!.text?.isEmpty()!!) {
            // Set error text
            txtLayoutFullName!!.error = getString(R.string.erroremail)
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail?.text!!).matches()) {
            txtLayoutFullName!!.error = getString(R.string.checkYourEmail)
            return false
        }
        if (txtPassword!!.text?.isEmpty()!!){
            txtLayoutPassword.error = getString(R.string.errorpassword)
            return false
        }

        if (txtPassword?.length()!! < 5) {
            txtLayoutPassword!!.error = getString(R.string.mustBeAtLeast5)
            return false
        }
        // Clear error text
        txtLayoutFullName!!.error = null
        // Clear error text
        txtLayoutPassword.error = null
        return true
    }

    private fun clickSignIn() {
        val mainIntent = Intent(this, SigninActivity::class.java).apply {
        }
        startActivity(mainIntent)

    }
}

