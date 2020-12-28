package com.intelliteq.fea.ammocalculator.modifyCalculation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.ModifyWeaponAdapter
import com.intelliteq.fea.ammocalculator.adapters.ModifyWeaponListener
import com.intelliteq.fea.ammocalculator.databinding.FragmentModifyCalculationBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 */
class ModifyCalculationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentModifyCalculationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_modify_calculation, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSourceCalculation = AmmoRoomDatabase.getAppDatabase(application)!!.calculationDao
        val dataSourceSinlge =
            AmmoRoomDatabase.getAppDatabase(application)!!.perWeaponCalculationDao
        val arguments = ModifyCalculationFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = ModifyCalculationViewModelFactory(
            arguments.calculationKey,
            arguments.days,
            arguments.intensity,
            arguments.name,
            dataSourceCalculation
        )

        val modifyCalculationViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ModifyCalculationViewModel::class.java)

        //binding
        binding.lifecycleOwner = this
        binding.modifyCalcFragment
        binding.modifyCalcViewModel
        binding.modifyName.text = arguments.name
        binding.daysOriginal.text = arguments.days.toString()
        binding.intensityOriginal.text = arguments.intensity

        //adapter
        val adapter = ModifyWeaponAdapter(ModifyWeaponListener {
        }, arguments.calculationKey, dataSourceSinlge)

        modifyCalculationViewModel.weapons.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.RecyclerViewWeaponsModify.adapter = adapter

        //navigate to home button
        binding.editHome.setOnClickListener {
            it.findNavController()
                .navigate(ModifyCalculationFragmentDirections.ActionModifyCalculationFragmentToLandingScreen())
        }

        //navigate to calculation output
        binding.calculationViewButton.setOnClickListener {
            it.findNavController().navigate(
                ModifyCalculationFragmentDirections
                    .actionModifyCalculationFragmentToCalculationOutputScreen(
                        arguments.calculationKey, arguments.days, arguments.intensity
                    )
            )
        }

        //save the changes button
        binding.saveModify.setOnClickListener { view ->
            modifyCalculationViewModel.onSave()
            view.findNavController().navigate(
                ModifyCalculationFragmentDirections
                    .actionModifyCalculationFragmentToCalculationOutputScreen(
                        arguments.calculationKey,
                        modifyCalculationViewModel.daysChangedValue.value!!,
                        modifyCalculationViewModel.intensityChangedValue.value!!
                    )
            )
        }

        //picker
        binding.pickerDaysModify.setOnValueChangedListener { _, _, newVal ->
            modifyCalculationViewModel.getHowManyDays(newVal)
        }

        //Combat Intensity spinner
        binding.spinnerCombatModify.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val combat = parent.getItemAtPosition(position)
                    modifyCalculationViewModel.assaultIntensityStringToIntValues(combat as String)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        binding.pickerDaysModify.setOnValueChangedListener { _, _, newVal ->
            modifyCalculationViewModel.getHowManyDays(newVal)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}