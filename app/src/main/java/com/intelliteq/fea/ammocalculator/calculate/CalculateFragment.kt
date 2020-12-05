package com.intelliteq.fea.ammocalculator.calculate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentCalculateBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


class CalculateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentCalculateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_calculate, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao
        val dataSourceAmmo = AmmoRoomDatabase.getAppDatabase(application)!!.ammoDao
        val dataSourceComp = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val dataSourceCalculation =
            AmmoRoomDatabase.getAppDatabase(application)!!.perWeaponCalculationDao
        val dataSourceCalculations = AmmoRoomDatabase.getAppDatabase(application)!!.calculationDao
        val arguments = CalculateFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = CalculateViewModelFactory(
            arguments.calcKey,
            dataSourceAmmo, dataSourceComp,
            dataSourceCalculation, dataSourceCalculations
        )
        val calculateViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(CalculateViewModel::class.java)

        //setting the binding values
        binding.calculateViewModel = calculateViewModel
        binding.lifecycleOwner = this

        binding.pickerWeapons.setOnValueChangedListener { _, _, newVal ->
            calculateViewModel.getNumberOfWeapons(newVal)
        }

        binding.pickerDays.setOnValueChangedListener { _, _, newVal ->
            calculateViewModel.getHowManyDays(newVal)
        }

        binding.reset.setOnClickListener {
                view: View -> view.findNavController()
            .navigate(CalculateFragmentDirections.ActionCalculateSelectionSelf(-1))
        }



        calculateViewModel.weapons.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.spinnerFea
                binding.spinnerDesc
                binding.spinnerAmmoType
                binding.spinnerType
                binding.spinnerCompAmmo
                binding.spinnerCompType
                binding.spinnerCombat
            }
        })


        //to calculate screen
        calculateViewModel.navigateToOutput.observe(
            viewLifecycleOwner,
            Observer { calc ->
                calc?.let {
                    this.findNavController()
                        .navigate(CalculateFragmentDirections
                            .ActionCalculateSelectionToCalculationOutputScreen(
                                calc.calculationId, calc.numberOfDays, calc.assaultIntensity))
                    calculateViewModel.doneNavigationToOutput()
                  //  Log.i("Calc frag","/////$calc")
                }

            }
        )

        //back to input more weapon
        calculateViewModel.navigateToAddAnotherWeaponForCalculation.observe(viewLifecycleOwner,
        Observer { calc ->
            calc?.let {
                this.findNavController()
                    .navigate(CalculateFragmentDirections.actionCalculateSelectionSelf(
                        calc.group_calculationID))
                calculateViewModel.doneNavigationToAddAnother()
            }
        })


        //FEA spinner
        binding.spinnerFea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val int = parent.getItemAtPosition(position)
                calculateViewModel.useWeaponFea(int as Int)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Description spinner
        binding.spinnerDesc.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val int = parent.getItemAtPosition(position)
                calculateViewModel.useWeaponDesc(int as String)
                //Log.i("Weapon Desc", "$int")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //Type spinner
        binding.spinnerType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val int = parent.getItemAtPosition(position)
                calculateViewModel.useWeaponType(int as String)
                // Log.i("Weapon Type", "$int")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        //Component Spinner
        binding.spinnerCompType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val int = parent.getItemAtPosition(position)
                    Log.i("Type5", "//** CompID $int")
                    calculateViewModel.useComponent(int as String)
                 //   calculateViewModel.doneSpinnerComp()
                     Log.i("Weapon Type", "CompID $int")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        //Combat Intensity spinner
        binding.spinnerCombat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val combat = parent.getItemAtPosition(position)
                calculateViewModel.assaultIntensityStringToIntValues(combat as String)
                // Log.i("Weapon Combat", "$combat")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        //ComponentAmmo spinner
        binding.spinnerCompAmmo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener  {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val comp = parent.getItemAtPosition(position)
                calculateViewModel.useComponentAmmo(comp as String)
              //  Log.i("error", "comp ammo: $comp")
             //   Log.i("error", "position: $position")
                //calculateViewModel.doneSpinnerCompAmmo()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
               // Log.i("error", "nothing selected")
            }

        }

        //Ammo spinner
        binding.spinnerAmmoType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val comp = parent.getItemAtPosition(position)
                calculateViewModel.useAmmo(comp as String)
              //  Log.i("Ammo1", "weapon ammo: $comp")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        return binding.root
    }

}