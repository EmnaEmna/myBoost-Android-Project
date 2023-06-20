package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.ProfilPassword
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.ProfileName
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.User
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

const val NAME = "NAME"
const val EMAIL = "EMAIL"


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var btnEditName: Button? = null
    private var btnEditPassword: Button? = null

    private var txtNewName: EditText? = null
    private var txtMail: EditText? = null
    private var txtNewPassword: EditText? = null
    private var TxtNewMail2: EditText? = null
    @Inject
    lateinit var services:ApiInterface


    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView :View = inflater.inflate(R.layout.fragment_profile, container, false)

        btnEditName = rootView.findViewById(R.id.btnEditName)
        btnEditPassword = rootView.findViewById(R.id.btnEditPassword)

        txtNewName = rootView.findViewById(R.id.txtNewName)
        txtMail = rootView.findViewById(R.id.txtMail)
        txtNewPassword = rootView.findViewById(R.id.txtNewPassword)
        TxtNewMail2 = rootView.findViewById(R.id.TxtNewMail2)

        btnEditName!!.setOnClickListener {
            editName()
        }

        btnEditPassword!!.setOnClickListener {
            ChangePassowrd()
        }

        return rootView
    }

    private fun editName() {
        //if (inputcheck()) {
        //Toast.makeText(this, "check ok ", Toast.LENGTH_SHORT).show()


        val userup = ProfileName(txtNewName!!.text.toString(),txtMail!!.text.toString())


        //val response = ServiceBuilder.buildService(ApiInterface::class.java)

       services.editName(userup).enqueue(
            object : Callback<ProfileName> {

                override fun onResponse(
                    call: Call<ProfileName>,
                    response: Response<ProfileName>
                ) {
                    val test = response.body()
                    if (test != null){
                        Toast.makeText(context, "Name updated successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Name update failed", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ProfileName>, t: Throwable) {
                    //Toast.makeText(this@ProfileFragment,t.toString(), Toast.LENGTH_LONG).show()
                }

            }
        )
    }

    private fun  ChangePassowrd(){

        //if (inputcheck()) {
        //Toast.makeText(this, "check ok ", Toast.LENGTH_SHORT).show()

        val userup1 = ProfilPassword(txtNewPassword!!.text.toString(),TxtNewMail2!!.text.toString())
        //val response = ServiceBuilder.buildService(ApiInterface::class.java)

        services.resetPassword(userup1).enqueue(
            object : Callback<ProfilPassword> {

                override fun onResponse(
                    call: Call<ProfilPassword>,
                    response: Response<ProfilPassword>
                ) {
                    val test = response.body()
                    if (test != null){
                        Toast.makeText(context, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context, "Password update failed", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<ProfilPassword>, t: Throwable) {
                    //Toast.makeText(this@ProfileFragment,t.toString(), Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(name: String, email: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
                    putString(EMAIL, email)
                }
            }
    }
}