package com.intelliteq.fea.ammocalculator.adapters

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.databinding.ListItemModifyWeaponBinding
import com.intelliteq.fea.ammocalculator.persistence.daos.CalculationDao
import com.intelliteq.fea.ammocalculator.persistence.daos.PerWeaponCalculationDao
import com.intelliteq.fea.ammocalculator.persistence.models.Calculation
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import kotlinx.coroutines.*


class ModifyWeaponAdapter(
    val clickListener: ModifyWeaponListener,
    val calculationKey: Long,
    val calculation: PerWeaponCalculationDao

//val textWatcher: ModifyWeaponTextWatcher
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



            binding.editTextWeaponNumber.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //Log.i("adapt3", "HERE")
                    if (p0.toString() != "") {
                        Log.i("adapt3", "changed to: ${p0.toString()}")
                        //Job and CoroutineScope
                        val viewModelJob = Job()
                        val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

                        //     var singleCalcFun = PerWeaponCalculation()


                        suspend fun getPerCalcFromDatabaseSuspend(): PerWeaponCalculation {
                            return withContext(Dispatchers.IO) {
                                val thiscalc = calculation.getUsingWeaponAndCalcID(
                                    calculationKey,
                                    item.weaponId //change from componentAutoID
                                )
                                Log.i("text4", "this calc: $thiscalc")
                                thiscalc
                            }
                        }

                        suspend fun updatePerCalcWeaponDatabaseSuspend(
                            thisCalc: PerWeaponCalculation,
                            num: String
                        ) {
                            withContext(Dispatchers.IO) {
                                thisCalc.numberOfWeapons = Integer.parseInt(num)
                                Log.i("adapt4", "1: ${thisCalc}")
                                calculation.update(thisCalc)
                                Log.i("adapt4", "2: ${thisCalc}")
                            }
                        }

                        fun getCalculationItem() {
                            uiScope.launch {
                                singleCalc = getPerCalcFromDatabaseSuspend()
                                Log.i("adapt3", "RTRN $singleCalc") //returning correct perCalc
                                Log.i("adapt3", "#1 ${p0.toString()}")
                                // binding.origCount.text = singleCalc.numberOfWeapons.toString()
                                Log.i("adapt5", "PerCalc: ${singleCalc}")
                                updatePerCalcWeaponDatabaseSuspend(singleCalc, p0.toString())
                            }
                        }

                        getCalculationItem()
                        //
                        Log.i("adapt3", "#2 $p0")


                    }

                    //  binding.origCount.text = singleCalc.numberOfWeapons.toString()

                    Log.i("adapt3", "compID: ${item.componentAutoId}")
                    Log.i("adapt3", "key: $calculationKey")


                }


            })

            // binding.origCount.text = singleCalc.numberOfWeapons.toString()


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
                    //returning correct perCalc
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
            //   Log.i("error", "same? $oldItem and new: $newItem")
            return oldItem.componentAutoId == newItem.componentAutoId
        }

        override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
            return oldItem == newItem
        }

    }

}

class ModifyWeaponListener(val clickListener: (saved: Component) -> Unit) {
    fun onClick(component: Component) {
        Log.i("adapt3", "Weapon/Comp $component")
        clickListener(component)

    }
}


class ModifyWeaponTextWatcher(val textWatcher: (saved: Int) -> Unit) {
    fun onChange(component: Int) {
        Log.i("adapt", "comp: $component")
        textWatcher(component)
    }
}