package com.intelliteq.fea.ammocalculator.calculate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.component.ComponentFragmentArgs
import com.intelliteq.fea.ammocalculator.component.ComponentFragmentDirections
import com.intelliteq.fea.ammocalculator.component.ComponentViewModel
import com.intelliteq.fea.ammocalculator.component.ComponentViewModelFactory
import com.intelliteq.fea.ammocalculator.databinding.FragmentCalculateBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_calculate.*


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

        val dataSourceWeapon = AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao
        val dataSourceAmmo = AmmoRoomDatabase.getAppDatabase(application)!!.weaponAmmoDao
        val dataSourceComp = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val dataSourceCompAmmo = AmmoRoomDatabase.getAppDatabase(application)!!.componentAmmoDao

        //creating a view model using the factory
        val viewModelFactory = CalculateViewModelFactory(dataSourceWeapon, dataSourceAmmo, dataSourceComp, dataSourceCompAmmo, application)
        val calculateViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(CalculateViewModel::class.java)

        //setting the binding values
        binding.calculateViewModel = calculateViewModel
        binding.lifecycleOwner = this

        binding.pickerWeapons.setOnValueChangedListener { picker, oldVal, newVal ->
            calculateViewModel.getWeaponNumber(newVal)
        }

        binding.pickerDays.setOnValueChangedListener { pickerDays, oldVal, newVal ->
            calculateViewModel.getDayNumber(newVal)
        }

        calculateViewModel.weapons.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.spinnerFea
                binding.spinnerDesc
                binding.spinnerType
                binding.spinnerCombat
            }
        })

//        calculateViewModel.ammos.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                binding.spinnerAmmoType
//            }
//        })

//        calculateViewModel.chosenWeapon.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                binding.spinnerFea
//                binding.spinnerDesc
//                binding.spinnerType
//                binding.spinnerCombat
//
//            }
//        })
//
//        calculateViewModel.chosenAmmo.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                binding.spinnerAmmoType
//            }
//        })
//
//        calculateViewModel.ammosList.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                binding.spinnerAmmoType
//                Log.i("Weapon FRAG", "//List $it")
//            }
//        })
//
//        binding.reset.setOnClickListener {
//                view: View -> view.findNavController().navigate(R.id.action_CalculateSelection_to_landingScreen)
//        }
//
//

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

//        calculateViewModel.chosenWeapon.observe(viewLifecycleOwner, Observer {
//            calculateViewModel.useWeaponFea(it.FEA_id)
//        })

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


        binding.spinnerCombat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val combat = parent.getItemAtPosition(position)
                calculateViewModel.useCombat(combat as String)
               // Log.i("Weapon Combat", "$combat")
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

//
//        binding.spinnerAmmoType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                val int = parent.getItemAtPosition(position)
//                calculateViewModel.useAmmoType(int as String)
//                //Log.i("Weapon Desc", "$int")
//            }
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }


        return binding.root
    }

}