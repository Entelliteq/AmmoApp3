package com.intelliteq.fea.ammocalculator.weapon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentWeaponBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * Fragment class for Weapon
 */
class WeaponFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        //binding variable and inflating the fragment
        val binding: FragmentWeaponBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weapon, container, false)

        //getting the application and database
        val application = requireNotNull(this.activity).application
        val dataSourceWeapon = AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao
        val dataSourceComponent = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao

        //creating a view model using the factory
        val viewModelFactory = WeaponViewModelFactory(dataSourceWeapon, dataSourceComponent, application)
        val weaponViewModel = ViewModelProvider(this, viewModelFactory)
            .get(WeaponViewModel::class.java)

        //creating a view model using the factory
        binding.setLifecycleOwner(this)
        binding.weaponViewModel = weaponViewModel

        //setting the navigation paths for the buttons
        //navigate to weapon ammo screen
        weaponViewModel.navigateToInputWeaponAmmo.observe(
            viewLifecycleOwner,
            Observer { weapon ->
                weapon?.let {
                    this.findNavController()
                        .navigate(WeaponFragmentDirections.WeaponInputToAmmoInput(weapon.weaponAutoId))
                 // Log.i("Wpn", "$weapon //add ammo ->")
                    weaponViewModel.doneNavigation()
                }

        })

        // Inflate the layout for this fragment
        return binding.root
    }
}