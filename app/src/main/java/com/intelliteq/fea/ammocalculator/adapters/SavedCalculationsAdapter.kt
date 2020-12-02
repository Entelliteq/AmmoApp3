package com.intelliteq.fea.ammocalculator.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.intelliteq.fea.ammocalculator.databinding.ListItamSavedCalculationsBinding
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation

class SavedCalculationsAdapter(val clickListener: SavedCalculationsListener) :
    ListAdapter<Calculation, SavedCalculationsAdapter.ViewHolder>(SavedCalculationDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }



    class ViewHolder(val binding: ListItamSavedCalculationsBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(
            item: Calculation,
            clickListener: SavedCalculationsListener
        ) {

            binding.savedClickListener = clickListener
            binding.savedCalcListItem = item
            binding.executePendingBindings()

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ListItamSavedCalculationsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    class SavedCalculationDiffCallback : DiffUtil.ItemCallback<Calculation>() {
        override fun areItemsTheSame(oldItem: Calculation, newItem: Calculation): Boolean {
            return oldItem.calculationId == newItem.calculationId
        }

        override fun areContentsTheSame(oldItem: Calculation, newItem: Calculation): Boolean {
            return oldItem == newItem
        }

    }

}

class SavedCalculationsListener(val clickListener: (saved: Calculation) -> Unit) {
    fun onClick(calculation: Calculation) {
        Log.i("days11", "adapter $calculation" )
        clickListener(calculation) }
}