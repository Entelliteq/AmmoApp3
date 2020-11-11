package com.intelliteq.fea.ammocalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemValidateComponentBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import androidx.recyclerview.widget.ListAdapter

class ValidateComponentAdapter :
    ListAdapter<Component, ValidateComponentAdapter.ViewHolder>(ValidateComponentDiffCallback()
) {
//    var data = listOf<Component>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//
//    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemValidateComponentBinding) :
        RecyclerView.ViewHolder(binding.root) {
//        val type: TextView = itemView.confCompTypDODIC
//        val desc: TextView = itemView.confCompDesc


        fun bind(
            item: Component
        ) {

            // type.text = item.
//            desc.text = item.componentDescription
//            type.text = item.componentTypeId


            binding.componentValidateListItem = item
            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemValidateComponentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    class ValidateComponentDiffCallback : DiffUtil.ItemCallback<Component>() {
        override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
          //  Log.i("error", "same? $oldItem and new: $newItem")
            return oldItem.componentAutoId == newItem.componentAutoId
        }

        override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem == newItem
        }

    }

}