package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemSavedWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Component

class SavedWeaponsAdapter(private val clickListener: SavedWeaponsListener) :
    ListAdapter<Component, SavedWeaponsAdapter.ViewHolder>(SavedWeaponsDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position)!!, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemSavedWeaponsBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnLongClickListener {

        fun bind(
            item: Component,
            clickListener: SavedWeaponsListener
        ) {



            binding.savedWeaponsClickListener = clickListener
            binding.savedWeaponListItem = item
            binding.executePendingBindings()

        }



        init {
            itemView.setOnLongClickListener(this)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItemSavedWeaponsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            Log.i("LONG", "again")
            return true
        }


    }

    class SavedWeaponsDiffCallback : DiffUtil.ItemCallback<Component>() {
        override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem.weaponId == newItem.weaponId
        }

        override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem == newItem
        }

    }

}

class SavedWeaponsListener(val clickListener: (saved: Component) -> Unit) {
    fun onClick(weapon: Component) {
        clickListener(weapon)
        Log.i("LONG", "Short Adapter")
    }

}

