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
 * A simple [Fragment] subclass.
 * Use the [ComponentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComponentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding : FragmentComponentInputBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_component_input, container, false )

        val application = requireNotNull(this.activity).application
        val arguments = ComponentFragmentArgs.fromBundle(arguments)

        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao

        val viewModelFactory = ComponentViewModelFactory(arguments.weaponKey, dataSource)

        val componentViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(ComponentViewModel::class.java)

        binding.lifecycleOwner = this
        binding.componentViewModel = componentViewModel

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

        componentViewModel.navigateToInputComponentAmmo.observe(
            viewLifecycleOwner,
            Observer { comp ->
                comp?.let {
                    this.findNavController()
                        .navigate((ComponentFragmentDirections.ComponentInputToAmmoInput(comp.componentId)))
                    componentViewModel.doneNavigatingToComponentAmmo()
                }
            }
        )

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