package com.intelliteq.fea.ammocalculator.deleteWeapon

import android.os.Bundle
import android.util.Log
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
import com.intelliteq.fea.ammocalculator.databinding.FragmentDeleteWeaponBinding
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditWeaponBinding
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponFragmentArgs
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponViewModel
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponViewModelFactory
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import java.util.logging.Handler


/**
 * A simple [Fragment] subclass.
 * Use the [DeleteWeaponFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeleteWeaponFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentDeleteWeaponBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_delete_weapon, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSourceComponent = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val ammoDao = AmmoRoomDatabase.getAppDatabase(application)!!.ammoDao
        val calculationDao = AmmoRoomDatabase.getAppDatabase(application)!!.calculationDao
        val perWeaponCalculationDao = AmmoRoomDatabase.getAppDatabase(application)!!.perWeaponCalculationDao
        val arguments = DeleteWeaponFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = DeleteWeaponViewModelFactory(
            arguments.weaponKey, dataSourceComponent, ammoDao, calculationDao, perWeaponCalculationDao)

        val deleteWeaponViewModel = ViewModelProvider(this, viewModelFactory)
            .get(DeleteWeaponViewModel::class.java)


        //binding
        binding.lifecycleOwner = this
        binding.deleteWeaponViewModel = deleteWeaponViewModel

        binding.buttonCancelDelete.setOnClickListener {
            findNavController()
                .navigate(DeleteWeaponFragmentDirections
                    .ActionDeleteWeaponFragmentToSavedWeaponsFragment())
        }


        deleteWeaponViewModel.weapon.observe(viewLifecycleOwner, Observer {

            Log.i("BLANK", "$it")
            if (it != null) {
                binding.confWpnDescDelete.text = it.componentDescription
                binding.confWpnTypeDelete.text = it.componentTypeId
            }
        })


        binding.buttonConfirmDelete.setOnClickListener {

            deleteWeaponViewModel.weapon.observe(viewLifecycleOwner, Observer {
                Log.i("WEEP2", it!!.componentTypeId)
                deleteWeaponViewModel.deleteComponent(it)
            })

            deleteWeaponViewModel.ammosWeapon.observe(viewLifecycleOwner, Observer {
                for (a in it) {
                    deleteWeaponViewModel.deleteAmmo(a)
                }
            })

            deleteWeaponViewModel.ammosComp.observe(viewLifecycleOwner, Observer {
                for(a in it) {
                    deleteWeaponViewModel.deleteAmmo(a)
                }
            })

            deleteWeaponViewModel.components.observe(viewLifecycleOwner, Observer {
                for(c in it) {
                    deleteWeaponViewModel.deleteComponent(c)
                }
            })

            deleteWeaponViewModel.calculation.observe(viewLifecycleOwner, Observer {
                for(c in it) {
                    deleteWeaponViewModel.deleteCalculation(c)
                }
            })

            Toast.makeText(activity, "Weapon is deleted", Toast.LENGTH_SHORT).show()
            findNavController()
                .navigate(DeleteWeaponFragmentDirections
                    .ActionDeleteWeaponFragmentToSavedWeaponsFragment())
        }




        // Inflate the layout for this fragment
        return binding.root
    }


}