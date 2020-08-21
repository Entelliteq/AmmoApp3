package com.intelliteq.fea.ammocalculator.componentAmmo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentComponentAmmoBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * Fragment class for ComponentAmmo
 */
class ComponentAmmoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentComponentAmmoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_component_ammo, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application
        val arguments = ComponentAmmoFragmentArgs.fromBundle(arguments)
        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.componentAmmoDao

        //creating a view model using the factory
        val viewModelFactory =
            ComponentAmmoViewModelFactory(arguments.componentKey, arguments.weaponKey, dataSource)
        val componentAmmoViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(ComponentAmmoViewModel::class.java)

        //setting the binding values
        binding.lifecycleOwner = this
        binding.componentAmmoViewModel = componentAmmoViewModel

        //setting the navigation paths for the buttons
        //checking for valid input of edit texts
        componentAmmoViewModel.checkStatusOfInputs.observe(
            viewLifecycleOwner,
            Observer { status ->
                status?.let {
                    if (!status) Toast.makeText(
                        activity,
                        "All fields need filled",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        )

        //navigating to confirmation screen
        componentAmmoViewModel.navigateToConfirmation.observe(
            viewLifecycleOwner,
            Observer { comp ->
                comp?.let {
                    this.findNavController()
                        .navigate(ComponentAmmoFragmentDirections.ComponentAmmoToVerify(comp.weaponIdComponentAmmo))
                    componentAmmoViewModel.doneNavigatingToVerify()
                }
            }
        )

        //navigate to back to componentAmmo screen
        componentAmmoViewModel.navigateToInputAnotherComponentAmmo.observe(
            viewLifecycleOwner,
            Observer {
                this.findNavController()
                    .navigate(
                        ComponentAmmoFragmentDirections.ComponentAmmoInputToSelf(
                            it.componentId,
                            it.weaponIdComponentAmmo
                        )
                    )
                componentAmmoViewModel.doneNavigatingToCompAmmo()
            }
        )

        // Inflate the layout for this fragment
        return binding.root
    }

}