package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.ListItemSavedWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import kotlinx.android.synthetic.main.list_item_saved_weapons.view.*

class SavedWeaponsEditDeleteAdapter(
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<SavedWeaponsEditDeleteAdapter.ViewHolder>() {

    var list = listOf<Component>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = list[position]
        holder.type.text = currentItem.componentTypeId
        holder.description.text = currentItem.componentDescription

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_saved_weapons, parent, false)
        return ViewHolder(itemView)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnLongClickListener {

        val type: TextView = itemView.typeTextOutput_saved
        val description: TextView = itemView.descriptionOut_saved


        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }


        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(position, list[position])
            }
         //   Log.i("Long", "long adapter ${list[position].weaponId}, ${list[position].componentTypeId}")
            return true
        }


        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position, list[position])
            }
         //   Log.i("Long", "short adapter ${list[position].weaponId}")
        }


    }


    interface OnItemClickListener {
        fun onItemClick(position: Int, weapon: Component)
        fun onItemLongClick(position: Int,  weapon: Component)
    }


}



