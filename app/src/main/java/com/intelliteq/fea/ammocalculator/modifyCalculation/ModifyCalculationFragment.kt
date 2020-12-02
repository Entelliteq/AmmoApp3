package com.intelliteq.fea.ammocalculator.modifyCalculation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.LandingScreenFragmentDirections
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.*
import com.intelliteq.fea.ammocalculator.databinding.FragmentComponentInputBinding
import com.intelliteq.fea.ammocalculator.databinding.FragmentModifyCalculationBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import com.intelliteq.fea.ammocalculator.persistence.models.PerWeaponCalculation
import com.intelliteq.fea.ammocalculator.savedCalculations.SavedCalculationsFragmentDirections
import kotlinx.android.synthetic.main.list_item_modify_weapon.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [ModifyCalculationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModifyCalculationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentModifyCalculationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_modify_calculation, container,false)

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
            dataSourceCalculation,
            dataSourceSinlge )

        val modifyCalculationViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ModifyCalculationViewModel::class.java)


        binding.lifecycleOwner = this
        binding.modifyCalcFragment
        binding.modifyCalcViewModel



        val adapter = ModifyWeaponAdapter(ModifyWeaponListener {
            weapon ->
          // Toast.makeText(context, "${weapon}", Toast.LENGTH_LONG).show()
            Log.i("adapt", "$weapon")
        }, arguments.calculationKey, dataSourceSinlge)




        binding.RecyclerViewWeaponsModify.adapter = adapter


        binding.editHome.setOnClickListener {
            it.findNavController()
                .navigate(ModifyCalculationFragmentDirections.ActionModifyCalculationFragmentToLandingScreen())
        }

        binding.modifyName.text = arguments.name

        modifyCalculationViewModel.weapons.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)


            }
        })

        binding.saveModify.setOnClickListener { view ->
           // Log.i("days11", "from bind")
            modifyCalculationViewModel.onSave()
            view.findNavController().navigate(R.id.action_ModifyCalculationFragment_to_savedCalculationsFragment)

        }

        binding.pickerDaysModify.setOnValueChangedListener { pickerDays, oldVal, newVal ->
            Log.i("days11", "old value: $oldVal")
            modifyCalculationViewModel.getHowManyDays(newVal)
        }


        //Combat Intensity spinner
        binding.spinnerCombatModify.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val combat = parent.getItemAtPosition(position)
                modifyCalculationViewModel.assaultIntensityStringToIntValues(combat as String)
                Log.i("Weapon Combat", "$combat")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.daysOriginal.text = arguments.days.toString()
        binding.intensityOriginal.text = arguments.intensity


        binding.pickerDaysModify.setOnValueChangedListener { pickerDays, oldVal, newVal ->
            modifyCalculationViewModel.getHowManyDays(newVal)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

}