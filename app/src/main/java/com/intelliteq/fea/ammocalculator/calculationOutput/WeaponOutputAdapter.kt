package com.intelliteq.fea.ammocalculator.calculationOutput

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.SingleWeaponCalculation
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import kotlinx.android.synthetic.main.list_item_weapons.view.*


class WeaponOutputAdapter: RecyclerView.Adapter<WeaponOutputAdapter.ViewHolder>() {

    var data = listOf<Weapon>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quantity = listOf<SingleWeaponCalculation>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val quantity = quantity[position]
        holder.bind(item, quantity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder.from(parent)
    }

    class ViewHolder(val binding: ListItemWeaponsBinding) : RecyclerView.ViewHolder(binding.root) {
        val type : TextView = itemView.type_output_tv
        val fea: TextView = itemView.fea_out_tv
        val desc: TextView = itemView.desc_out_tv
        val quantity: TextView = itemView.quantity_out_tv

        fun bind(
            item: Weapon,
            single: SingleWeaponCalculation
        ) {
            val res = itemView.context.resources
            type.text = item.weaponTypeID
            fea.text = item.FEA_id.toString()
            desc.text = item.weaponDescription
            quantity.text = single.numberOfWeapons.toString()


            //binding.weaponOutputListItem = item
           // binding.executePendingBindings()

        }

        companion object {
            fun from (parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeaponsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}