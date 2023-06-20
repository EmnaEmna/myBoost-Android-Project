package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.fragment.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var bottombar : BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bottombar= findViewById(R.id.bottom_navigation)
        //bottombar.selectedItemId=R.id.home


        bottombar.setOnItemSelectedListener{ item ->
            when ( item.itemId ) {
                R.id.home -> {
                    changeFragment(HomeGroupFragment())
                    true

                }
                R.id.profile -> {

                    changeFragment(ProfileFragment())
                    true
                }

                R.id.settings -> {
                    changeFragment(SettingsFragment())
                    true
                }

                R.id.chat -> {
                    //val intentchat = Intent(this@MainActivity, EntranceActivity::class.java)
                    //startActivity(intentchat)
                    changeFragment(ChatFragment())
                    true
                }

                /*R.id.project -> {
                    changeFragment(ProjectFragment())
                    true
                }*/

                //else -> changeFragment(ChatFragment())
                else -> false


            }
        }

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, HomeGroupFragment()).commit()
    }

    private fun changeFragment(fragment: Fragment){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        //fragmentTransition.replace(R.id.fragment_container,fragment).addToBackStack(Fragment::class.java.simpleName).commit()
        fragmentTransition.replace(R.id.fragment_container,fragment).addToBackStack("").commit()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuAddGroup){
            val addIntent = Intent(this, BuildTeamProjectActivity::class.java)
            startActivity(addIntent)
        }else{
            val addIntent = Intent(this, AddProjectActivity::class.java)
            startActivity(addIntent)
        }
        return super.onOptionsItemSelected(item)
    }


}
