package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.R

class HelloActivity : AppCompatActivity() {
    lateinit var hellousername: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        supportActionBar?.hide()
        hellousername=findViewById(R.id.helloname)


        val PREF_USERNAME = getSharedPreferences("PREF_USERNAME", MODE_PRIVATE)
        val value = PREF_USERNAME.getString("PREF_USERNAME", "")
        hellousername.setText(value)
    }

}