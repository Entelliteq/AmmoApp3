package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import kotlinx.android.synthetic.main.list_item_weapons.view.*


class WeaponOutputAdapter : RecyclerView.Adapter<WeaponOutputAdapter.ViewHolder>() {

    var data = listOf<Component>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var quantity = listOf<PerWeaponCalculation>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("err2", "weapon size ${data.size}")
        Log.i("err2", "perCalc size ${quantity.size}")
        val item = data[position]
        val per = quantity[position]

        holder.bind(item, per)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemWeaponsBinding) : RecyclerView.ViewHolder(binding.root) {
        val type: TextView = itemView.type_output_tv
        val fea: TextView = itemView.fea_out_tv
        val desc: TextView = itemView.desc_out_tv
        val quantity: TextView = itemView.quantity_out_tv

        fun bind(
            item: Component,
            per: PerWeaponCalculation
        ) {
            val res = itemView.context.resources
            type.text = item.componentTypeId
            fea.text = item.FEA_id.toString()
            desc.text = item.componentDescription
            quantity.text = per.numberOfWeapons.toString()


//            binding.weaponOutputListItem = item
//            binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemWeaponsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}