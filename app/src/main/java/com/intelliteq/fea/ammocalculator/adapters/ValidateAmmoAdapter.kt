package com.intelliteq.fea.ammocalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemValidateAmmoBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import androidx.recyclerview.widget.ListAdapter

class ValidateAmmoAdapter :
    ListAdapter<Ammo, ValidateAmmoAdapter.ViewHolder>(ValidateAmmoDiffCallback()) {



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // Log.i("Adapter lists", " ammos: ${data.size},")
        holder.bind(item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemValidateAmmoBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            item: Ammo
        ) {


            binding.ammoValidateListItem = item
            binding.executePendingBindings()

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemValidateAmmoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    class ValidateAmmoDiffCallback : DiffUtil.ItemCallback<Ammo>() {
        override fun areItemsTheSame(oldItem: Ammo, newItem: Ammo): Boolean {
         //   Log.i("error", "same? $oldItem and new: $newItem")
            return oldItem.ammoAutoId == newItem.ammoAutoId
        }

        override fun areContentsTheSame(oldItem: Ammo, newItem: Ammo): Boolean {
            return oldItem == newItem
        }

    }

}