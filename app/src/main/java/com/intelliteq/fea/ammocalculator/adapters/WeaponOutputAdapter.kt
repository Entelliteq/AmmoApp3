package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.calculationOutput.WeaponCardData
import com.intelliteq.fea.ammocalculator.databinding.ListItemWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import kotlinx.android.synthetic.main.list_item_weapons.view.*


class WeaponOutputAdapter : ListAdapter<WeaponCardData, WeaponOutputAdapter.ViewHolder>(ViewHolder.WeaponOutputDiffCallback()){

    var cards = listOf<WeaponCardData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
//
//    var quantity = listOf<PerWeaponCalculation>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cards
//        val per = quantity[position]
        Log.i("adapt3 Adapter", "$item")
        holder.bind(item[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(val binding: ListItemWeaponsBinding) : RecyclerView.ViewHolder(binding.root) {
        val type: TextView = itemView.type_output_tv
        val fea: TextView = itemView.fea_out_tv
        val desc: TextView = itemView.desc_out_tv
        val quantity: TextView = itemView.quantity_out_tv

        fun bind(
            item: WeaponCardData
          //  per: PerWeaponCalculation
        ) {
           itemView.context.resources
            type.text = item.type
            fea.text = item.fea.toString()
            desc.text = item.description
            quantity.text = item.quantity.toString()
//            binding.weaponOutputListItem = item
//            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeaponsBinding.inflate(
                    layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

        class WeaponOutputDiffCallback : DiffUtil.ItemCallback<WeaponCardData>() {
            override fun areItemsTheSame(oldItem: WeaponCardData, newItem: WeaponCardData): Boolean {
                return oldItem.fea == newItem.fea
            }

            override fun areContentsTheSame(oldItem: WeaponCardData, newItem: WeaponCardData): Boolean {
                return oldItem == newItem
            }
        }
    }
}