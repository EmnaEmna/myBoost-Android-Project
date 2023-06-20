package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs
import javax.inject.Inject

class EntranceActivity : AppCompatActivity() {

    lateinit var button: TextView
    lateinit var room_id: String;
    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrance)
        button = findViewById(R.id.button)
        try {
            room_id = intent.getStringExtra("room_id").toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val PREF_USERNAME = getSharedPreferences("PREF_USERNAME", MODE_PRIVATE)
        val value = PREF_USERNAME.getString("PREF_USERNAME", "")
        enterChatroom(value)
       // button.setOnClickListener(this)
    }

/*
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.button -> enterChatroom()
        }

    }
*/
    private fun enterChatroom(value: String?) {
        val userName = value
        val roomName = room_id

        if(!roomName.isNullOrBlank()&&!userName.isNullOrBlank()) {
            val intentchatroom = Intent(this@EntranceActivity, ChatRoomActivity::class.java)


            intentchatroom.putExtra("userName",userName)
            intentchatroom.putExtra("roomName",roomName)
            startActivity(intentchatroom)
            this.finish()
        }else{
            Toast.makeText(this,"Nickname and Roomname should be filled!", Toast.LENGTH_SHORT)
        }
    }
}
