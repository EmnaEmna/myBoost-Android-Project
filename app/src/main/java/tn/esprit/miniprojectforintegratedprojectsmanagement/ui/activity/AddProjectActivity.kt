package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.TypeAdapter
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Group
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Project
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.fragment.ProjectFragment
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

@AndroidEntryPoint
class AddProjectActivity : AppCompatActivity() {
    private var btnSaveProject: Button? = null
    private lateinit var txtLayoutProjectName: TextInputLayout
    private var txtProjectName: TextInputEditText? = null
    private lateinit var emailSpinner: Spinner
    private lateinit var txtLayoutlinkgit: TextInputLayout
    private var txtlinkgit: TextInputEditText? = null
    private lateinit var txtLayoutcomment: TextInputLayout
    private var txtcomment: TextInputEditText? = null
    lateinit var txtDeadline: TextInputEditText
    lateinit var emailSpinnerlinearL: LinearLayout
    lateinit var txtLayoutcalender: TextInputLayout

    private var ggcc: Button? = null


    @Inject
    lateinit var services: ApiInterface
    @Inject
    lateinit var sharedPrefs: SharedPrefs


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_add)

        btnSaveProject = findViewById(R.id.btnSaveProject)
        txtLayoutProjectName = findViewById(R.id.txtLayoutProjectName)
        txtProjectName = findViewById(R.id.txtProjectName)
        txtLayoutlinkgit = findViewById(R.id.txtLayoutlinkgit)
        txtlinkgit = findViewById(R.id.txtlinkgit)
        txtLayoutcomment = findViewById(R.id.txtLayoutcomment)
        txtcomment = findViewById(R.id.txtcomment)

        emailSpinnerlinearL= findViewById(R.id.emailSpinnerlinearL)

        txtLayoutcalender= findViewById(R.id.txtLayoutcalender)

        ggcc=findViewById(R.id.ggcc)


        txtDeadline = findViewById(R.id.txtDeadline)

        emailSpinner = findViewById(R.id.emailSpinner)

// Fetch the list of emails from the backend
        fetchEmails()
      //  testFetchEmails()

        val toolbar: Toolbar = findViewById(R.id.app_bar)
        val startDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .build()
        startDatePicker.addOnPositiveButtonClickListener {
            txtDeadline.setText(startDatePicker.headerText.toString())
        }

        txtDeadline.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                startDatePicker.show(supportFragmentManager, "DEADLINE_DATE")
            } else {
                startDatePicker.dismiss()
            }
        }

        toolbar.setNavigationIcon(R.drawable.ic_back)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = " let's Go to work "

        val userRole = sharedPrefs.getUserRole()

        if (userRole == "manager") {
            emailSpinnerlinearL.visibility = View.VISIBLE
            txtLayoutcalender.visibility = View.VISIBLE


        } else {
            emailSpinnerlinearL.visibility = View.GONE
            txtLayoutcalender.visibility = View.GONE

        }

        btnSaveProject!!.setOnClickListener {
            addProject()
            val assignedTo = emailSpinner.selectedItem.toString()

            intent.putExtra("assignedTo", assignedTo)

        }

        ggcc!!.setOnClickListener{
            services.makeGoogleRequest().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    // Handle the response of the first request
                    val responseBody = response.body()?.string()
                    // Perform any necessary operations with the response data
                    // Make the second request to /schedule_event
                    services.makeScheduleEventRequest().enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            // Handle the response of the second request
                            val responseBody2 = response.body()?.string()
                            // Perform any necessary operations with the response data
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            // Handle any errors for the second request
                            t.printStackTrace()
                        }
                    })
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle any errors for the first request
                    t.printStackTrace()
                }
            })
        }

    }



    private fun testFetchEmails() {
        val emails = arrayOf("a", "b", "c")

        val adapter = ArrayAdapter(
            this@AddProjectActivity,
            android.R.layout.simple_spinner_item,
            emails
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emailSpinner.adapter = adapter
    }


    private fun fetchEmails() {
        val bundle = intent.extras
        val groupId = bundle?.getString("groupId")
        Log.d("Tag", "groupidddddddddddddddddddddddddddddddddddddddd: $groupId") // Log the emails

        // Call the getEmails API and update the spinner with the fetched emails
        if (groupId != null) {
            services.getEmails(groupId).enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(call: Call<MutableList<String>>, response: Response<MutableList<String>>) {
                    val emails = response.body()
                    Log.d("Tag", "Response Boooooooody: ${response.body().toString()}")

                    Log.d("Tag", "emaiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiils mails fetch")
                    Log.d("Tag", emails.toString())
                    Log.d("Tag", "Emails: ${emails?.toMutableList()}") // Log the emails

                    print("emaiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiils" + emails)
                    if (emails != null) {
                        // Create an ArrayAdapter and set it as the adapter for the emailSpinner
                        val adapter = ArrayAdapter(
                            this@AddProjectActivity,
                            android.R.layout.simple_spinner_item,
                            emails.toMutableList()
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        emailSpinner.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.d("Tag", "faaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaailure mails fetch")
                    Log.e("Tag", "Error: ${t.message}")

                    Toast.makeText(
                        this@AddProjectActivity,
                        "Failed to fetch emails: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun addProject() {
        //if (inputcheck()) {
        //Toast.makeText(this, "check ok ", Toast.LENGTH_SHORT).show()

        //get params from bundle
        val bundle = intent.extras
        val groupId = bundle?.getString("groupId")
        val status = "todo"
        val proj = Project(
            "",
            txtProjectName!!.text.toString(),
            txtlinkgit!!.text.toString(),
            txtcomment!!.text.toString(),
            groupId!!,
            txtDeadline!!.text.toString(),
            emailSpinner.selectedItem.toString(),
            status
        )


        //val response = ServiceBuilder.buildService(ApiInterface::class.java)
        val assignedTo = emailSpinner.selectedItem.toString()

        services.createproject(proj).enqueue(object : Callback<Project> {
            override fun onResponse(
                call: Call<Project>, response: Response<Project>
            ) {
                val test = response.body()
                Log.d("Tag", "task connnnnnnnnnnnnnnntent: $test") // Log the emails

                if (test != null) {
                    Toast.makeText(
                        this@AddProjectActivity, "add task Success", Toast.LENGTH_SHORT
                    ).show()

                    val assignedTo = emailSpinner.selectedItem.toString()
                    val intent = Intent(this@AddProjectActivity, EditTaskActivity::class.java)
                    intent.putExtra("assignedTo", assignedTo)
                    println("assignedTotooooooooooooooooooooooooooooooooooooooooooooooo888888addprojectactivity: ${assignedTo}")

                    val intentmain = Intent(this@AddProjectActivity, MainActivity::class.java)

                    startActivity(intentmain)
                } else {
                    Toast.makeText(
                        this@AddProjectActivity, "add assignement error", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                Toast.makeText(this@AddProjectActivity, t.toString(), Toast.LENGTH_LONG).show()
            }

        })
        // }

    }


}