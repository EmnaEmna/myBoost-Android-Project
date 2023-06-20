package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.ActivityBuildTeamProjectBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.FragmentHomeGroupBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Group
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

@AndroidEntryPoint
class BuildTeamProjectActivity : AppCompatActivity() {
    private var btnSaveProjectTeam: Button? = null
    private lateinit var txtProjectName: TextInputEditText
    private lateinit var txtEmailUser1: TextInputEditText
    private lateinit var txtEmailUser2: TextInputEditText
    private lateinit var txtEmailUser3: TextInputEditText
    private lateinit var txtEmailUser4: TextInputEditText
    private lateinit var txtEmailUser5: TextInputEditText
    private lateinit var txtEmailUser10: TextInputEditText
    private lateinit var txtEmailUser20: TextInputEditText
    private lateinit var txtClass: TextInputEditText
    private lateinit var txtYear: TextInputEditText
    private lateinit var binding: ActivityBuildTeamProjectBinding
    @Inject
    lateinit var services:ApiInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_build_team_project)
        txtProjectName = findViewById(R.id.txtProjectName)
        txtEmailUser1 = findViewById(R.id.txtEmailUser1)
        txtEmailUser2 = findViewById(R.id.txtEmailUser2)
        txtEmailUser3 = findViewById(R.id.txtEmailUser3)
        txtEmailUser4 = findViewById(R.id.txtEmailUser4)
        txtEmailUser5 = findViewById(R.id.txtEmailUser5)
        txtEmailUser10 = findViewById(R.id.txtEmailUser10)
        txtEmailUser20 = findViewById(R.id.txtEmailUser20)
        txtClass = findViewById(R.id.txtClass)
        txtYear = findViewById(R.id.txtYear)

        btnSaveProjectTeam = findViewById(R.id.btnSaveProjectTeam)
        btnSaveProjectTeam!!.setOnClickListener {
            save();
        }
binding  = ActivityBuildTeamProjectBinding.inflate(layoutInflater)

    }

    private fun save() {
        //if (inputcheck()) {
        Toast.makeText(this, "check ok ", Toast.LENGTH_SHORT).show()

        val  textemails : MutableList<String> = arrayListOf()
        textemails.add(txtEmailUser1.text.toString())
        textemails.add(txtEmailUser2.text.toString())
        textemails.add(txtEmailUser3.text.toString())
        textemails.add(txtEmailUser4.text.toString())
        textemails.add(txtEmailUser5.text.toString())
        textemails.add(txtEmailUser10.text.toString())
        textemails.add(txtEmailUser20.text.toString())

        //var  sendToken:String = null

        val newgroupe = Group("",txtProjectName.text.toString(),txtClass.text.toString(),txtYear.text.toString(),textemails)

        services.addgroup(newgroupe).enqueue(
            object : Callback<Group> {
                override fun onResponse(
                    call: Call<Group>,
                    response: Response<Group>
                ) {
                    val test = response.body()
                    if (test != null){
                        Toast.makeText(this@BuildTeamProjectActivity,"create Success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@BuildTeamProjectActivity, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this@BuildTeamProjectActivity, "create error", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Group>, t: Throwable) {
                    Toast.makeText(this@BuildTeamProjectActivity,t.toString(), Toast.LENGTH_LONG).show()
                }

            }
        )
    }

}