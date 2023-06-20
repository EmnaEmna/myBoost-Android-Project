package tn.esprit.miniprojectforintegratedprojectsmanagement.adapters

import android.R
import android.content.Intent
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.ProjectSingleRowBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Project
import tn.esprit.miniprojectforintegratedprojectsmanagement.sharedPrefs.SharedPrefs
import tn.esprit.miniprojectforintegratedprojectsmanagement.ui.activity.EditTaskActivity
import tn.esprit.miniprojectforintegratedprojectsmanagement.utils.ApiInterface
import javax.inject.Inject

class ProjectAdapter(val intent: Intent?,val onItemClicked: (Project) -> Unit, ) : RecyclerView.Adapter<ProjectAdapter.MyViewHolder>() {
//*************
@Inject
lateinit var services: ApiInterface
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    //private var onEditButtonClickListener: ((Project) -> Unit)? = null
    private var onEditButtonClickListener: ((Project, String?) -> Unit)? = null


//    fun setOnEditButtonClickListener(listener: (Project) -> Unit) {
//        onEditButtonClickListener = listener
//    }
fun setOnEditButtonClickListener(listener: (Project, String?) -> Unit) {
    onEditButtonClickListener = listener
}
    //*****************
    private var list: MutableList<Project> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            ProjectSingleRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)


    }

    fun updateList(list: MutableList<Project>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        //get device key

        return list.size
    }

    inner class MyViewHolder(val binding: ProjectSingleRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var assignedTo: String? = null

        init {
            binding.editButton.setOnClickListener {

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val project = list[position]
//                    onEditButtonClickListener?.invoke(project)
                    onEditButtonClickListener?.invoke(project, assignedTo)

                }
            }
        }
        fun bind(item: Project) {
            binding.txtProjectName.text = item.name
            binding.txtlinkgit.text = item.gitlink
            binding.textcomment.text = item.text
            binding.deadline.text = item.deadline


            binding.root.setOnClickListener {
                assignedTo = item.assignedTo
                println("assignedTotooooooooooooooooooooooooooooooooooooooooooooooo   lahnééé no:  ${assignedTo}")




                println("project id: ${item.id}")
                Log.d("MyTag", "Ceci est un message de ediiiiiiiiiiiiiiiiiiiiiiiiiiiit ok2")

                onItemClicked(item)

            }


            binding.editButton.setOnClickListener {
                //****
                assignedTo = item.assignedTo
                println("assignedTotoooooooooooooooooooooooooooooooooooooooooooooooediit3houni houni houni : ${assignedTo}")



                println("project id: ${item.id}")
                Log.d("MyTag", "Ceci est un message de ediiiiiiiiiiiiiiiiiiiiiiiiiiiit okeeedit3")

                onItemClicked(item)
                //*****
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val project = list[position]

                    val intent = Intent(binding.root.context, EditTaskActivity::class.java)
                    // Passer les informations du projet en tant qu'extra
                    intent.putExtra("project_id", project.id)
                    intent.putExtra("project_name", project.name)
                          intent.putExtra("project_gitlink", project.gitlink)

                    intent.putExtra("project_description", project.text)
                            intent.putExtra("project_deadline", project.deadline)
                    intent.putExtra("assignedTo", project.assignedTo)

                    // Ajoutez d'autres extras en fonction des données que vous souhaitez passer

                    binding.root.context.startActivity(intent)
                }
            }
        }

    }

}