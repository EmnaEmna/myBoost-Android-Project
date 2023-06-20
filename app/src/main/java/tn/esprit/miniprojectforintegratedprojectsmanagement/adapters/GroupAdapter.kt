package tn.esprit.miniprojectforintegratedprojectsmanagement.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.miniprojectforintegratedprojectsmanagement.databinding.GroupSingleRowBinding
import tn.esprit.miniprojectforintegratedprojectsmanagement.models.Group

class GroupAdapter(private val products: MutableList<Group>, val onItemClick: (Group) -> Unit) :
    RecyclerView.Adapter<GroupAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            GroupSingleRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = products[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
        // holder.productImage.setImageResource(currentItem.image!!.toInt())
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateList(mProducts: List<Group>) {
        products.clear()
        products.addAll(mProducts)
        notifyDataSetChanged()
    }


    class MyViewHolder(val binding: GroupSingleRowBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {

            binding.txtCalssroom.text = group.classroom
            binding.txtGrouptName.text = group.name
            binding.txtYear.text = group.year

            val builder = StringBuilder()
            for (groups in group.emails!!) {
                builder.append(groups)
                builder.append("\n")
            }

            binding.txtEmails.text = builder.toString()
        }


    }
}