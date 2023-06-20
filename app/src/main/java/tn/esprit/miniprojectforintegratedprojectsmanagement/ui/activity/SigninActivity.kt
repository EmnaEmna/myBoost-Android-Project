package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.util.Patterns
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.User
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

@AndroidEntryPoint
class SigninActivity : AppCompatActivity() {

    @Inject
    lateinit var services:ApiInterface
    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private var btnSignIn: Button? = null
    private lateinit var txtLayoutFullName: TextInputLayout
    private lateinit var txtLayoutPassword: TextInputLayout
    private lateinit var txtLayoutEmail: TextInputLayout
    private var txtFullName: TextInputEditText? = null
    private var txtPassword: TextInputEditText? = null
    private var txtEmail: TextInputEditText? = null
    //lateinit var txtId: TextInputEditText
    private var radioButtonManager: RadioButton? = null
    private var radioButtonEmployer: RadioButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        btnSignIn = findViewById(R.id.btnSignIn)
        txtLayoutFullName = findViewById(R.id.txtLayoutFullName)
        txtLayoutPassword = findViewById(R.id.txtLayoutPassword)
        txtLayoutEmail = findViewById(R.id.txtLayoutEmail)
        txtFullName = findViewById(R.id.txtFullName)
        txtPassword = findViewById(R.id.txtPassword)
        txtEmail = findViewById(R.id.txtEmail)
       // txtId = findViewById(R.id.txtId)
        radioButtonManager = findViewById(R.id.radioButtonManager)
        radioButtonEmployer = findViewById(R.id.radioButtonEmployer)



        val toolbar: Toolbar = findViewById(R.id.app_bar)
        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        supportActionBar?.title = "    Let's Start"
        btnSignIn!!.setOnClickListener {
            clicknext();
        }
    }

    private fun inputcheck(): Boolean {

        txtLayoutFullName.error = null
        txtLayoutPassword.error = null
        if (txtFullName!!.text?.isEmpty()!!) {
            // Set error text
            txtLayoutFullName.error = getString(R.string.errorname)

            return false
        }
        if (txtFullName?.length()!! < 3) {
            txtLayoutFullName!!.error = getString(R.string.mustBeAtLeast3)
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

        if (txtEmail!!.text?.isEmpty()!!){
            txtLayoutEmail.error = getString(R.string.erroremil)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail?.text!!).matches()) {
            txtLayoutEmail!!.error = getString(R.string.checkYourEmail)
            return false
        }
        /*else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail!!.text).matches()) {
            txtEmail!!.error = getString(R.string.errormailvalid)
            txtLayoutEmail.error = null
            return false
        }*/
        // Clear error text
        txtLayoutFullName.error = null
        // Clear error text
        txtLayoutPassword.error = null
        txtLayoutEmail.error = null
        return true
    }

    private fun clicknext() {
        if (inputcheck()) {
            Toast.makeText(this, "check ok ", Toast.LENGTH_SHORT).show()
            val userRole = if (radioButtonManager!!.isChecked) {
                "manager"
            } else {
                "employer"
            }
            val userup = User(txtEmail!!.text.toString(),txtPassword!!.text.toString(),"",txtFullName!!.text.toString(),userRole)
            services.signup(userup).enqueue(
                object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        val test = response.body()
                        if (test != null){
                           sharedPrefs.saveToken(test.token!!)
                            if (test.role != null) {
                                sharedPrefs.saveUserRole(test.role!!)
                            } else {
                                // Gérer le cas où le rôle est nul
                            }


                            sharedPrefs.saveUserEmail(test.email)
                            Toast.makeText(this@SigninActivity,"signup Success", Toast.LENGTH_SHORT).show()
                            // Redirect to the desired activity
                            val intent = Intent(this@SigninActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish() // Close the current activity
                        }else{
                            Toast.makeText(this@SigninActivity, "signup error", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@SigninActivity,t.toString(),Toast.LENGTH_LONG).show()
                        println("error: "+t.message)
                        println("error: $t")
                        println("error: ${t.localizedMessage}")
                    }

                }
            )
        }

    }


}