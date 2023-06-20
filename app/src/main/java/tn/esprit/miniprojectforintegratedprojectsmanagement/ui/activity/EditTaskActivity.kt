package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.ActivityEditTaskBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Project
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject
import retrofit2.Callback
import retrofit2.Call
import tn.esprit.miniprojectforintegratedprojectsmanagement.adapters.ProjectAdapter
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs


@AndroidEntryPoint
class EditTaskActivity : AppCompatActivity() {
    private var btnEditProject: Button? = null

    private lateinit var emailSpinner: Spinner
    private lateinit var txtDeadline: TextInputEditText
    lateinit var radioGroupStatus: RadioGroup
    lateinit var radioButtonDoing: RadioButton
    lateinit var radioButtonDone: RadioButton
    lateinit var emailSpinnerlinearL: LinearLayout
    lateinit var txtLayoutcalender: TextInputLayout

    @Inject
    lateinit var services: ApiInterface
    private lateinit var binding: ActivityEditTaskBinding
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)
        val assignedTo = intent.getStringExtra("assignedTo") ?: ""
        println("assignedTotooooooooooooooooooooooooooooooooooooooooooooooo888888: ${assignedTo}")


        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnEditProject = binding.btnEditProject
        radioGroupStatus = findViewById(R.id.radioGroupStatus)
        radioButtonDoing = findViewById(R.id.radioButtonDoing)
        radioButtonDone=findViewById(R.id.radioButtonDone)

        emailSpinner = binding.emailSpinner
        txtDeadline = binding.txtDeadline

        val projectId = intent.getStringExtra("project_id") ?: ""
        val projectName = intent.getStringExtra("project_name") ?: ""
        val projectGitLink = intent.getStringExtra("project_gitlink") ?: ""
        val projectDescription = intent.getStringExtra("project_description") ?: ""
        val projectDeadline = intent.getStringExtra("project_deadline") ?: ""

        // Utiliser les informations pour afficher les données du projet dans les champs correspondants
        binding.txtProjectName.setText(projectName)
        binding.txtlinkgit.setText(projectGitLink)
        binding.txtcomment.setText(projectDescription)
       binding.txtDeadline.setText(projectDeadline)
        emailSpinnerlinearL= findViewById(R.id.emailSpinnerlinearL)

        txtLayoutcalender= findViewById(R.id.txtLayoutcalender)
        //datepicekr
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

        val userRole = sharedPrefs.getUserRole()

        if (userRole == "employer") {
            radioGroupStatus.visibility = View.VISIBLE
            txtLayoutcalender.visibility = View.GONE
            emailSpinnerlinearL.visibility = View.GONE

        } else {
            radioGroupStatus.visibility = View.GONE
            txtLayoutcalender.visibility = View.VISIBLE
            emailSpinnerlinearL.visibility = View.VISIBLE


        }


        //****
        //**** afficher le contenue de l'email recupéré depuis l'apterproject, qui est deja recupéré depuis le addprojectactivity
        // Récupérez la référence du Spinner à partir du binding
        val aa= binding.emailSpinner
        // Créez un adaptateur pour le Spinner en utilisant un ArrayAdapter avec une seule valeur
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.add(assignedTo)
        // Définissez le style du Spinner déroulant
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Attachez l'adaptateur au Spinner
        aa.adapter = adapter
            // Définissez la sélection du Spinner sur le premier élément (indice 0)
        aa.setSelection(0)
        //***
        // binding.emailSpinner.setSelection(assignedTo)
        val spinnerAssignedTo = binding.emailSpinner
      //  val assignedTo = spinnerAssignedTo.selectedItem.toString()
        // Set the selected email in the spinner
        val emailAdapter = spinnerAssignedTo.adapter as? ArrayAdapter<String>
        val position = emailAdapter?.getPosition(assignedTo)
        position?.let { spinnerAssignedTo.setSelection(it) }




        //get params from bundle
        val bundle = intent.extras
       val groupId = bundle?.getString("groupId") ?: ""
        var status = "for"
        val selectedRadioButtonId = radioGroupStatus.checkedRadioButtonId
        if (selectedRadioButtonId == radioButtonDoing.id) {
            status = "doing"
        } else if (selectedRadioButtonId == radioButtonDone.id) {
            status = "done"
        }


        val curentuser = sharedPrefs.getUserEmail()
        println("curentuser ooooooooooooooooooooooooooooooooooooooooooooooo: $curentuser")
        if (userRole == "manager") {
            binding.btnEditProject.isEnabled = true
       }else{
            if (curentuser != null && assignedTo != null && curentuser.equals(assignedTo, ignoreCase = true)) {
                binding.btnEditProject.isEnabled = true
            } else {
                binding.btnEditProject.isEnabled = false
                Toast.makeText(binding.root.context, "This task is not assigned to you.", Toast.LENGTH_SHORT).show()


            }
        }

//        radioGroupStatus.setOnCheckedChangeListener { _, checkedId ->
//            val selectedRadioButton: RadioButton = findViewById(checkedId)
//
//            // Traiter le statut sélectionné ici
//            if (selectedRadioButton == radioButtonDoing) {
//                // Définir le statut comme "doing"
//                status = "doing"
//
//            } else if (selectedRadioButton == radioButtonDone) {
//                // Définir le statut comme "done"
//                status = "done"
//            }
//        }

        Log.d("Status", "Status houuuuni: $status")
// Mettre à jour le projet avec le statut récupéré
        val proj = Project(
            projectId,
            binding.txtProjectName.text.toString(),
            binding.txtlinkgit.text.toString(),
            binding.txtcomment.text.toString(),
            groupId,
            binding.txtDeadline.text.toString(),
            emailSpinner.selectedItem.toString(),
            status
        )





        btnEditProject!!.setOnClickListener {
                updateProject(projectId,proj )
                val assignedTo = emailSpinner.selectedItem.toString()
                intent.putExtra("assignedTo", assignedTo)

            }
        }


    private fun updateProject(projectId: String, proj: Project) {
        // Get params from bundle
        val bundle = intent.extras
        val groupId = bundle?.getString("groupId") ?: ""
        Log.d("Tag", "groupidddddddddddddddddddddddddddddddddddddddd edit task edddddiiiit juste pour le proj instance!: $groupId")



        val selectedRadioButtonId = radioGroupStatus.checkedRadioButtonId
        if (selectedRadioButtonId == radioButtonDoing.id) {
            proj.status = "doing"
        } else if (selectedRadioButtonId == radioButtonDone.id) {
           proj.status = "done"
        }


        sharedPrefs.saveTaskStatus(proj.status)




        proj.name = binding.txtProjectName!!.text.toString()
        proj.gitlink = binding.txtlinkgit!!.text.toString()
        proj.text = binding.txtcomment!!.text.toString()

        proj.deadline = binding.txtDeadline!!.text.toString()
        proj.assignedTo = emailSpinner.selectedItem.toString()



        Log.d("Tag", "Updating project with ID: $projectId")
        Log.d("Tag", "New project details: $proj")

        val call = services.updateProject(projectId, proj)
        call.enqueue(object : Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    val updatedProject = response.body()
                    if (updatedProject != null) {
                        // Handle the successful update of the project
                        // Example: showSuccessMessage()
                        val assignedTo = emailSpinner.selectedItem.toString()

                        val intent = Intent(this@EditTaskActivity, ProjectAdapter::class.java)
                        intent.putExtra("assignedTo", assignedTo)
                        println("assignedTotooooooooooooooooooooooooooooooooooooooooooooooo888888addprojectactivity: $assignedTo")

                        val intentMain = Intent(this@EditTaskActivity, MainActivity::class.java)

                        startActivity(intentMain)

                        Log.d("Tag", "Project update successful")
                        showToast("Project updated successfully")
                    }
                } else {
                    // Handle the error case when the update request fails
                    // Example: displayErrorMessage()
                    Log.e("Tag", "Project update failed with response code: ${response.code()}")
                    showToast("Failed to update project")
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                // Handle the failure case when the API call throws an exception
                // Example: displayErrorMessage()
                Log.e("Tag", "Project update failed: ${t.message}")
                showToast("Project update failed")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    private fun updateProject(projectId: String, proj: Project) {
//        //get params from bundle
//        val bundle = intent.extras
//        val groupId = bundle?.getString("groupId")
//        Log.d("Tag", "groupidddddddddddddddddddddddddddddddddddddddd edit task  edddddiiiit juste pour le proj instance!: $groupId")
//
//        val proj = Project(
//            "",
//           binding.txtProjectName!!.text.toString(),
//            binding.txtlinkgit!!.text.toString(),
//            binding.txtcomment!!.text.toString(),
//            groupId!!,
//            binding.txtDeadline!!.text.toString(),
//            emailSpinner.selectedItem.toString(),
//            //selectedStatus
//        )
//        val call = services.updateProject(projectId, proj)
//        call.enqueue(object : Callback<Project> {
//            override fun onResponse(call: Call<Project>, response: Response<Project>) {
//                if (response.isSuccessful) {
//                    val proj = response.body()
//                    if (proj != null) {
//                        // Handle the successful update of the project
//                        // Example: showSuccessMessage()
//                        val assignedToupdate = emailSpinner.selectedItem.toString()
//
//                        val intent = Intent(this@EditTaskActivity, ProjectAdapter::class.java)
//                        intent.putExtra("assignedTo", assignedToupdate)
//                        println("assignedTotooooooooooooooooooooooooooooooooooooooooooooooo888888addprojectactivity: ${assignedToupdate}")
//
//                        val intentmain = Intent(this@EditTaskActivity, MainActivity::class.java)
//
//                        startActivity(intentmain)
//
//                    }
//                } else {
//                    // Handle the error case when the update request fails
//                    // Example: displayErrorMessage()
//                }
//            }
//
//            override fun onFailure(call: Call<Project>, t: Throwable) {
//                // Handle the failure case when the API call throws an exception
//                // Example: displayErrorMessage()
//            }
//        })
//    }




}