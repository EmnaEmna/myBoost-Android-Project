package tn.esprit.miniprojectforintegratedprojectsmanagement.ui.fragment
import android.util.Log

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.R
import tn.esprit.miniprojectforintegratedprojectsmanagement.adapters.ProjectAdapter
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.FragmentProjectBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.GroupId
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Project
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity.AddProjectActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity.EditTaskActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

@AndroidEntryPoint
class ProjectFragment : Fragment() {

    private var retrievedProjects: List<Project> = emptyList()

    private val adapter by lazy {
        val parentIntent = activity?.intent

        ProjectAdapter(parentIntent,onItemClicked = { })
    }
    lateinit var recyclerProjectView: RecyclerView
    lateinit var extendedFab: ExtendedFloatingActionButton
    lateinit var loadingPB: ProgressBar
    lateinit var tabLayout: TabLayout




    @Inject
    lateinit var services: ApiInterface
    lateinit var binding: FragmentProjectBinding
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var projectAdapter: ProjectAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProjectBinding.inflate(inflater, container, false)



        val rootView = inflater.inflate(R.layout.fragment_project, container, false)

        tabLayout =  rootView.findViewById(R.id.tabLayout)
        // Set up TabLayout with desired tabs
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("ToDo"))
        tabLayout.addTab(tabLayout.newTab().setText("Doing"))
        tabLayout.addTab(tabLayout.newTab().setText("Done"))
        extendedFab = rootView.findViewById(R.id.addProject)
        loadingPB = rootView.findViewById(R.id.idPBLoading)
        recyclerProjectView = rootView.findViewById(R.id.recyclerProjectView)
        currentTab = tabLayout.getTabAt(tabLayout.selectedTabPosition)?.text.toString()



        recyclerProjectView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Scroll down
                if (dy > 20 && extendedFab.isExtended) {
                    extendedFab.shrink()
                }

                // Scroll up
                if (dy < -20 && !extendedFab.isExtended) {
                    extendedFab.extend()
                }

                // At the top
                if (!recyclerView.canScrollVertically(-1)) {
                    extendedFab.extend()
                }
            }
        })
        // get parameters from bundle
        val bundle = this.arguments
        var groupId = ""
        if(bundle?.get("id") != null){
            groupId = bundle.get("id").toString()
        }
        extendedFab.setOnClickListener {
            Toast.makeText(context, "Add Task", Toast.LENGTH_SHORT).show()
            //navigate to activity and pass group id parameter
            val addProjectActivity = Intent(requireContext(), AddProjectActivity::class.java)
            addProjectActivity.putExtra("groupId", groupId)
            startActivity(addProjectActivity)
        }


        //show groupId when clicked on the groupp_roject in the previous fragment
        /////////Toast.makeText(requireContext(), groupId, Toast.LENGTH_SHORT).show()
        val grpId = GroupId(groupId)
        services.getProjects(grpId).enqueue(object : Callback<List<Project>> {
            override fun onResponse(
                call: Call<List<Project>>, response: Response<List<Project>>
            ) {
                loadingPB.visibility = View.GONE
                val projects = response.body()
                if (projects != null) {
                    val myLayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter.updateList(projects.toMutableList())
                    recyclerProjectView.layoutManager = myLayoutManager
                    recyclerProjectView.adapter = adapter
                    currentTab = "All"
                    retrievedProjects= projects  // Assign the retrieved projects

                    filterProjects()
                } else {
                    Toast.makeText(context, "No projects found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
            }
        })

        return rootView

    }
    // Fonction locale pour filtrer les projets en fonction de currentTab
    private fun filterProjects() {
        val filteredProjects = when (currentTab) {
            "All" -> retrievedProjects // Afficher tous les projets
            "ToDo" -> retrievedProjects.filter { it.status == "todo" }
            "Doing" -> retrievedProjects.filter { it.status == "doing" }
            "Done" -> retrievedProjects.filter { it.status == "done" }
            else -> emptyList() // Gérer les cas d'onglet inconnus
        }

        adapter.updateList(filteredProjects.toMutableList())
    }

    private lateinit var currentTab: String // Track the currently selected tab

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parentIntent = activity?.intent

        projectAdapter = ProjectAdapter(parentIntent,onItemClicked = { project ->
            // Handle item click for the specific project
            // You can access the project data and perform any required actions here
        })

        binding.recyclerProjectView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = projectAdapter
        }

        projectAdapter.setOnEditButtonClickListener { project,_ ->
            // Handle edit button click for the specific project
            // You can access the project data and perform any required actions here
            val intent = Intent(requireContext(), EditTaskActivity::class.java)
            intent.putExtra("projectId", project.id)
            Log.d("MyTag", "Ceci est un message de ediiiiiiiiiiiiiiiiiiiiiiiiiiiit ok")

            startActivity(intent)
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentTab = it.text.toString()
                    filterProjects()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}




