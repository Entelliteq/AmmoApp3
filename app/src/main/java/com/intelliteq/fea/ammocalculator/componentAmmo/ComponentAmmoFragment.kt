package com.intelliteq.fea.ammocalculator.componentAmmo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.component.ComponentFragmentDirections
import com.intelliteq.fea.ammocalculator.databinding.FragmentComponentAmmoBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [ComponentAmmoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComponentAmmoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentComponentAmmoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_component_ammo, container, false )

        val application = requireNotNull(this.activity).application
        val arguments = ComponentAmmoFragmentArgs.fromBundle(arguments)

        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.componentAmmoDao

         val viewModelFactory = ComponentAmmoViewModelFactory(arguments.componentKey, dataSource)

        val componentViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(ComponentAmmoViewModel::class.java)

        binding.lifecycleOwner = this
        binding.componentAmmoViewModel = componentViewModel


        componentViewModel.checkStatusOfInputs.observe(
            viewLifecycleOwner,
            Observer { status ->
                status?.let {
                    if (!status) Toast.makeText(activity, "All fields need filled", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        )

        componentViewModel.navigateToConfirmation.observe(
            viewLifecycleOwner,
            Observer {
                this.findNavController()
                    .navigate(ComponentAmmoFragmentDirections.ComponentAmmoToVerify(it))

            }
        )

        componentViewModel.navigateToInputAnotherComponentAmmo.observe(
            viewLifecycleOwner,
            Observer {
                this.findNavController()
                    .navigate(ComponentAmmoFragmentDirections.ComponentAmmoInputToSelf(it.componentId))
            }
        )

        // Inflate the layout for this fragment
        return binding.root
    }

}