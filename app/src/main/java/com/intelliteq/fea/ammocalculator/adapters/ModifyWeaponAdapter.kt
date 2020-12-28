package com.intelliteq.fea.ammocalculator.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemModifyWeaponBinding
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import kotlinx.coroutines.*


class ModifyWeaponAdapter(
    private val clickListener: ModifyWeaponListener,
    val calculationKey: Long,
    val calculation: PerWeaponCalculationDao
) :
    ListAdapter<Component, ModifyWeaponAdapter.ViewHolder>(ModifyWeaponDiffCallback()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener, calculationKey, calculation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder(val binding: ListItemModifyWeaponBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Component,
            clickListener: ModifyWeaponListener,
            calculationKey: Long,
            calculation: PerWeaponCalculationDao

        ) {

            var singleCalc = PerWeaponCalculation()
            binding.weaponModifyListItem = item
            binding.modifyWeaponClickListener = clickListener

            //Changing the quantity of weapons in the adapter cards
            binding.editTextWeaponNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString() != "") {
                        //Job and CoroutineScope
                        val viewModelJob = Job()
                        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

                        suspend fun getPerCalcFromDatabaseSuspend(): PerWeaponCalculation {
                            return withContext(Dispatchers.IO) {
                                val thiscalc = calculation.getUsingWeaponAndCalcID(
                                    calculationKey,
                                    item.weaponId //change from componentAutoID
                                )
                                thiscalc
                            }
                        }

                        suspend fun updatePerCalcWeaponDatabaseSuspend(
                            thisCalc: PerWeaponCalculation,
                            num: String
                        ) {
                            withContext(Dispatchers.IO) {
                                thisCalc.numberOfWeapons = Integer.parseInt(num)
                                calculation.update(thisCalc)
                            }
                        }

                        fun getCalculationItem() {
                            uiScope.launch {
                                singleCalc = getPerCalcFromDatabaseSuspend()
                                updatePerCalcWeaponDatabaseSuspend(singleCalc, p0.toString())
                            }
                        }
                        getCalculationItem()
                    }
                }
            })



            val viewModelJob = Job()
            val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

            suspend fun getPerCalcFromDatabaseSuspend(): PerWeaponCalculation {
                return withContext(Dispatchers.IO) {
                    val thiscalc = calculation.getUsingWeaponAndCalcID(
                        calculationKey,
                        item.weaponId
                    )
                    thiscalc
                }
            }


            fun getCalculationItem(): PerWeaponCalculation {
                uiScope.launch {
                    singleCalc = getPerCalcFromDatabaseSuspend()
                    binding.perWeaponCalculation = singleCalc
                }
                return singleCalc
            }
            singleCalc = getCalculationItem()
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemModifyWeaponBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

    }

    class ModifyWeaponDiffCallback : DiffUtil.ItemCallback<Component>() {
        override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem.componentAutoId == newItem.componentAutoId
        }

        override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem == newItem
        }

    }

}

class ModifyWeaponListener(val clickListener: (saved: Component) -> Unit) {
    fun onClick(component: Component) {
        clickListener(component)

    }
}


class ModifyWeaponTextWatcher(val textWatcher: (saved: Int) -> Unit) {
    fun onChange(component: Int) {
        textWatcher(component)
    }
}