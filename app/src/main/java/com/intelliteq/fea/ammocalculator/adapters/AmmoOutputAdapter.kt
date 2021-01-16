package com.intelliteq.fea.ammocalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.calculationOutput.AmmoCalculationCardData
import com.intelliteq.fea.ammocalculator.databinding.ListItemAmmoBinding
import kotlinx.android.synthetic.main.list_item_ammo.view.*

class AmmoOutputAdapter : RecyclerView.Adapter<AmmoOutputAdapter.ViewHolder>() {

    var cards = listOf<AmmoCalculationCardData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = cards.size // ammoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cards
        holder.bind(item[position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemAmmoBinding) : RecyclerView.ViewHolder(binding.root) {
        val desc: TextView = itemView.ammo_type_output_tv
        val dodic: TextView = itemView.dodic_out_tv
        private val perWeapon: TextView = itemView.per_weapon_out_tv
        private val totalAmmo: TextView = itemView.total_out_tv

        fun bind(
            item: AmmoCalculationCardData
        ) {
            itemView.context.resources
            desc.text = item.ammoDescription
            dodic.text = item.ammoDODIC
            perWeapon.text =
                item.perWeaponCalculation.toString()
            totalAmmo.text = item.totalPerAmmoCalculation.toString()

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemAmmoBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}