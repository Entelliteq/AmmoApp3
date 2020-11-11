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

//    var data = listOf<Ammo>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//
//
//    override fun getItemCount() = data.size

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
//        val desc: TextView = itemView.confWpnAmmoDesc
//        val dodic: TextView = itemView.confWpnAmmoTypDODIC
//        val train: TextView = itemView.confTrain
//        val sec: TextView = itemView.confSecurity
//        val sus: TextView = itemView.confSustain
//        val light: TextView = itemView.confLight
//        val medium: TextView = itemView.confMedium
//        val heavy: TextView = itemView.confHeavy

        fun bind(
            item: Ammo
        ) {

//            desc.text = item.ammoDescription
//            dodic.text = item.ammoDODIC
//            train.text = item.trainingRate.toString()
//            sec.text = item.securityRate.toString()
//            sus.text = item.sustainRate.toString()
//            light.text = item.lightAssaultRate.toString()
//            medium.text = item.mediumAssaultRate.toString()
//            heavy.text = item.heavyAssaultRate.toString()

            binding.ammoValidateListItem = item
            binding.executePendingBindings()

        }


//        fun intensityStringToValue(combat: String, item: Ammo): Int {
//            return when (combat) {
//                "Training" -> item.trainingRate
//                "Security" -> item.securityRate
//                "Sustain" -> item.sustainRate
//                "Light Assault" -> item.lightAssaultRate
//                "Medium Assault" -> item.mediumAssaultRate
//                else -> item.heavyAssaultRate
//            }
//
//        }

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