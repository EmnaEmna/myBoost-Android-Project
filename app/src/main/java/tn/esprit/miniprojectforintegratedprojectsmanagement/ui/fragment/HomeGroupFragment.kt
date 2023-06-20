package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.adapters.GroupAdapter
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.FragmentHomeGroupBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Group
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity.AddProjectActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity.BuildTeamProjectActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

@AndroidEntryPoint
class HomeGroupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var btn: Button

    @Inject
    lateinit var services: ApiInterface
    lateinit var binding: FragmentHomeGroupBinding
    lateinit var homeRecyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeGroupBinding.inflate(inflater, container, false)


        val addProjectGroupButton = binding.addProjectGroupbtn
        addProjectGroupButton.setOnClickListener {
            Toast.makeText(context, "Add Project Group", Toast.LENGTH_SHORT).show()
            //navigate to activity
            val addProjectActivity = Intent(requireContext(), BuildTeamProjectActivity::class.java)

            startActivity(addProjectActivity)


        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        getGroupeList()


////        btn = requireView().findViewById(R.id.testBtn)
//
//        btn.setOnClickListener{
//
//            println("aaaa  "  + getGroupeList())
//
//        }
    }

    private fun getGroupeList(): MutableList<Group> {
// create a group list
        val groupList = ArrayList<Group>()
        services.getgroups().enqueue(object : Callback<List<Group>> {
            override fun onResponse(call: Call<List<Group>>, response: Response<List<Group>>) {
                if (response.isSuccessful) {
                    val groups = response.body()


                    Toast.makeText(requireContext(), "groups", Toast.LENGTH_SHORT).show()
                    homeRecyclerView = binding.recyclerGroupView

                    homeRecyclerView.adapter = GroupAdapter(groups?.toMutableList()!!, onItemClick = {
                        println("aaaa "+ it.id)
                        //navigate to another fragment
                        val bundle = Bundle()
                        bundle.putString("id", it.id)
                        val fragment = ProjectFragment()
                        fragment.arguments = bundle
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit()

                    })
                    homeRecyclerView.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }

            override fun onFailure(call: Call<List<Group>>, t: Throwable) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                println("error $t")
            }
        })

        return groupList
    }

    fun setupRecyclerView() {


        //  val group = Group("aa", "aaa","&&&","aaa",null)

//val list= ArrayList<Group>()
        //      list.add(0,group)


    }
}