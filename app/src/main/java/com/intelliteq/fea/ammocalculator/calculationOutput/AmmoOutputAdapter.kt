package com.intelliteq.fea.ammocalculator.calculationOutput

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemAmmoBinding
import com.intelliteq.fea.ammocalculator.databinding.ListItemWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.SingleWeaponCalculation
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo
import kotlinx.android.synthetic.main.list_item_ammo.view.*
import kotlinx.android.synthetic.main.list_item_weapons.view.*

class AmmoOutputAdapter : RecyclerView.Adapter<AmmoOutputAdapter.ViewHolder>() {

    var data = listOf<Ammo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(val binding: ListItemAmmoBinding) : RecyclerView.ViewHolder(binding.root) {
        val type: TextView = itemView.ammo_type_output_tv
        val dodic: TextView = itemView.dodic_out_tv


        fun bind(
            item: Ammo
        ) {
            val res = itemView.context.resources
            type.text = item.ammoTypeID
            dodic.text = item.ammoDODIC



            //binding.weaponOutputListItem = item
            // binding.executePendingBindings()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAmmoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}