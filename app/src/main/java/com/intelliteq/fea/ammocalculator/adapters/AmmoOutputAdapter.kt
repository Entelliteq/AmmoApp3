package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemAmmoBinding
import com.intelliteq.fea.ammocalculator.formulas.BasicAmmoFormula
import com.intelliteq.fea.ammocalculator.formulas.BasicPerWeaponFormula
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import kotlinx.android.synthetic.main.list_item_ammo.view.*

class AmmoOutputAdapter : RecyclerView.Adapter<AmmoOutputAdapter.ViewHolder>() {


    //ammo
    var ammoList = listOf<Ammo>()
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.i("out1", "ammo: ${ammoList.size}")
        }

    //calculation
    var calc = listOf<Calculation>()
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.i("out1", "calc: ${calc.size}")
        }

    //single calc
    var perWeaponCalcList = listOf<PerWeaponCalculation>()
        set(value) {
            field = value
            notifyDataSetChanged()
            Log.i("out1", "each weapon calc ${perWeaponCalcList.size}")
        }

    override fun getItemCount() = ammoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //   Log.i("Size", "single calc: ${singleCalcList.size}")
    //    Log.i("Size", "ammo: ${ammoList.size}")
    //   Log.i("Size", "calculatio: ${calc.size}")


        val item = ammoList[position]
        val calcItem = calc[0] //always be 1
        val single = perWeaponCalcList[position/2]

    //    Log.i("Adapter lists", " ammos: ${ammoList.size}, calcs: ${calc.size}, singles: ${singleCalcList.size}")
        holder.bind(item, calcItem, single)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemAmmoBinding) : RecyclerView.ViewHolder(binding.root) {
        val desc: TextView = itemView.ammo_type_output_tv
        val dodic: TextView = itemView.dodic_out_tv
        val perWeapon: TextView = itemView.per_weapon_out_tv
        val totalAmmo: TextView = itemView.total_out_tv

        fun bind(
            item: Ammo,
            calcItem: Calculation,
            quantity: PerWeaponCalculation
        ) {
            val res = itemView.context.resources
            desc.text = item.ammoDescription
            dodic.text = item.ammoDODIC
            perWeapon.text = perWeapon(item, calcItem, quantity).toString()
            totalAmmo.text = totalAmmoCalc(item, calcItem, quantity).toString()


//            binding.weaponOutputListItem = item
//            binding.executePendingBindings()

        }

        fun totalAmmoCalc(item: Ammo, calcItem: Calculation, quantity: PerWeaponCalculation): Int {
            var total = 0
            var assault = calcItem.assaultIntensity
            val calculation = BasicAmmoFormula(intensityStringToValue(assault, item), calcItem.numberOfDays, quantity.numberOfWeapons)
            total = calculation.calculate()
            val temp = intensityStringToValue(assault, item)
          //  Log.i("ADAPTER" , "assault: $temp")
          //  Log.i("ADAPTER" , "fun ${intensityStringToValue(assault, item)}, ${calcItem.numberOfDays}, ${quantity.numberOfWeapons}")
            return total
        }

        fun perWeapon(item: Ammo, calcItem: Calculation, quantity: PerWeaponCalculation) : Int {
            var per = 0
            val perCalc = BasicPerWeaponFormula(totalAmmoCalc(item, calcItem, quantity), quantity.numberOfWeapons)
            per = perCalc.calculate()


            return per
        }

        fun intensityStringToValue(combat: String, item: Ammo): Int {
            return when (combat) {
                "Training" -> item.trainingRate
                "Security" -> item.securityRate
                "Sustain" -> item.sustainRate
                "Light Assault" -> item.lightAssaultRate
                "Medium Assault" -> item.mediumAssaultRate
                else -> item.heavyAssaultRate
            }

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