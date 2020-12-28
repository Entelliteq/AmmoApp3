package com.intelliteq.fea.ammocalculator.validation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.ModifyAmmoListener
import com.intelliteq.fea.ammocalculator.adapters.ModifyWeaponListener
import com.intelliteq.fea.ammocalculator.adapters.ValidateAmmoAdapter
import com.intelliteq.fea.ammocalculator.adapters.ValidateComponentAdapter
import com.intelliteq.fea.ammocalculator.databinding.FragmentValidationBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class ValidationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentValidationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_validation, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application
        val ammoDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.ammoDao
        val componentDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val arguments = ValidationFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = ValidationViewModelFactory(
            arguments.weaponKey, componentDatabase, ammoDatabase
        )

        val validateViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ValidationViewModel::class.java)

        //binding
        binding.validationViewModel
        binding.lifecycleOwner = this


        //adapters
        val weaponAmmoValidationAdapter = ValidateAmmoAdapter(ModifyAmmoListener {
            this.requireView().findNavController()
                .navigate(
                    ValidationFragmentDirections
                        .ActionConfirmationToEditAmmoFragment(it.ammoAutoId, it.weaponId))
        })

        binding.RecyclerViewAmmoConfirmation.adapter = weaponAmmoValidationAdapter

        val componentAmmoValidationAdapter = ValidateAmmoAdapter(ModifyAmmoListener {
            this.requireView().findNavController()
                .navigate(
                    ValidationFragmentDirections
                        .ActionConfirmationToEditAmmoFragment(it.ammoAutoId, it.weaponId))
        })

        binding.RecyclerViewComponentAmmoConfirmation.adapter = componentAmmoValidationAdapter

        val componentValidationAdapter = ValidateComponentAdapter(ModifyWeaponListener { weapon ->
            this.requireView().findNavController()
                .navigate(
                    ValidationFragmentDirections
                        .actionConfirmationToEditComponentFragment2(
                            weapon.componentAutoId,
                            weapon.weaponId )  )
        })

        binding.RecyclerViewComponentConfirmation.adapter = componentValidationAdapter

        //navigate to edit component
        binding.cardViewValidateWeapon.setOnClickListener {
            it.findNavController()
                .navigate(
                    ValidationFragmentDirections
                        .ActionConfirmationToEditComponentFragment(arguments.weaponKey)
                )
        }

        //navigate to calculate
        binding.save.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(ValidationFragmentDirections.ConfirmationToCalculate(-1))
        }

        //ValidateAmmoAdapter
        validateViewModel.ammosWeapon.observe(viewLifecycleOwner, Observer { ammos ->
            ammos?.let {
                weaponAmmoValidationAdapter.submitList(ammos)
            }
        })

        //ValidateAmmoAdapter
        validateViewModel.ammosComp.observe(viewLifecycleOwner, Observer { ammos ->
            ammos?.let {
                componentAmmoValidationAdapter.submitList(ammos)
                if (ammos.isEmpty()) binding.confirmCompAmmoTextView.visibility = View.GONE
            }
        })

        //ValidateComponentAdapter
        validateViewModel.components.observe(viewLifecycleOwner, Observer { comps ->
            comps?.let {
                componentValidationAdapter.submitList(comps)
                if (comps.isEmpty()) binding.confirmComponentTextView.visibility = View.GONE
            }
        })

        //Weapon
        validateViewModel.weapon.observe(viewLifecycleOwner, Observer { wep ->
            wep?.let {
                binding.confWpnType.text = wep.componentTypeId
                binding.confWpnDesc.text = wep.componentDescription
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }

}

