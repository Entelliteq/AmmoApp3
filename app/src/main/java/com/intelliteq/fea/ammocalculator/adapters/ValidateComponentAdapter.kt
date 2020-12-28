package com.intelliteq.fea.ammocalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemValidateComponentBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Component

class ValidateComponentAdapter(
    private val clickListener: ModifyWeaponListener
) :
    ListAdapter<Component, ValidateComponentAdapter.ViewHolder>(ValidateComponentDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(val binding: ListItemValidateComponentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(
            item: Component,
            clickListener: ModifyWeaponListener
        ) {
            binding.componentValidateListItem = item
            binding.modifyWeaponClickListener = clickListener
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

