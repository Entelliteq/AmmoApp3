package com.intelliteq.fea.ammocalculator.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentComponentInputBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * Fragment class for Component
 */
class ComponentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        //binding variable and inflating the fragment
        val binding : FragmentComponentInputBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_component_input, container, false )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application
        val arguments = ComponentFragmentArgs.fromBundle(arguments)
        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao

        //creating a view model using the factory
        val viewModelFactory = ComponentViewModelFactory(arguments.weaponKey, dataSource)
        val componentViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(ComponentViewModel::class.java)


        //setting the binding values
        binding.componentViewModel = componentViewModel
        binding.lifecycleOwner = this

        //setting the navigation paths for the buttons
        //navigating back to Component
        componentViewModel.navigateToAnotherComponent.observe(
            viewLifecycleOwner,
            Observer { weaponId ->
                weaponId?.let {
                    this.findNavController()
                        .navigate(ComponentFragmentDirections.ComponentInputToSelf(weaponId))
                    componentViewModel.doneNavigatingToAnotherComponent()
                }
            }
        )

        //navigating to component ammo screen
        componentViewModel.navigateToInputComponentAmmo.observe(
            viewLifecycleOwner,
            Observer { comp ->
                comp?.let {
                    this.findNavController() //changed order of input here ->
                        .navigate((ComponentFragmentDirections.ComponentInputToAmmoInput(comp.weaponId, comp.componentAutoId)))
                    componentViewModel.doneNavigatingToComponentAmmo()
                }
            }
        )

        //navigating to confirmation screen
        componentViewModel.navigateToConfirmation.observe(
            viewLifecycleOwner,
            Observer { weaponId ->
                weaponId?.let {
                    this.findNavController()
                        .navigate(ComponentFragmentDirections.ComponentToVerify(weaponId))
                    componentViewModel.doneNavigatingToConfirm()
                }
            }
        )


        // Inflate the layout for this fragment
        return binding.root
    }

}