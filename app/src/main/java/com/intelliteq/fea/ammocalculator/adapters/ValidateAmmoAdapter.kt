package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemValidateAmmoBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo

class ValidateAmmoAdapter(
    private val clickListener: ModifyAmmoListener
) :
    ListAdapter<Ammo, ValidateAmmoAdapter.ViewHolder>(ValidateAmmoDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
        Log.i("EDIT5", "$item")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemValidateAmmoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Ammo,
            clickListener: ModifyAmmoListener
        ) {
            binding.ammoValidateListItem = item
            binding.executePendingBindings()
            binding.modifyAmmoClickListener = clickListener
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemValidateAmmoBinding.inflate(
                    layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    class ValidateAmmoDiffCallback : DiffUtil.ItemCallback<Ammo>() {
        override fun areItemsTheSame(oldItem: Ammo, newItem: Ammo): Boolean {
            return oldItem.ammoAutoId == newItem.ammoAutoId
        }

        override fun areContentsTheSame(oldItem: Ammo, newItem: Ammo): Boolean {
            return oldItem == newItem
        }
    }
}

class ModifyAmmoListener(val clickListener: (saved: Ammo) -> Unit) {
    fun onClick(ammo: Ammo) {
        clickListener(ammo)

    }
}

