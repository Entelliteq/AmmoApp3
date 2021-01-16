package com.intelliteq.fea.ammocalculator.savedWeapons

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.KeyEventDispatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.*
import com.intelliteq.fea.ammocalculator.databinding.FragmentSavedWeaponsBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.savedCalculations.SavedCalculationsFragmentDirections


/**
 * A simple [Fragment] subclass.
 * Use the [SavedWeaponsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedWeaponsFragment : Fragment(), SavedWeaponsEditDeleteAdapter.OnItemClickListener {

    lateinit var  savedWeaponViewModel : SavedWeaponsViewModel ;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSavedWeaponsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_weapons, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val weapons = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao

        //creating a view model using the factory
        val viewModelFactory = SavedWeaponViewModelFactory(weapons)

        savedWeaponViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SavedWeaponsViewModel::class.java)

        //binding
        binding.savedWeaponViewModel
        binding.lifecycleOwner = this


        //navigate to modify screen
        savedWeaponViewModel.naviagteToModifyWeapon.observe(viewLifecycleOwner, Observer { weapon ->
            weapon?.let {
                SavedWeaponsFragmentDirections.ActionSavedWeaponsFragmentToViewEditDeleteFragment(
                    weapon.weaponId
                )
                savedWeaponViewModel.onModifyWeaponNavigated()
                Log.i("EDIT3", "3 $weapon")
            }
        })

        //adapter
//        val savedWeaponAdapter = SavedWeaponsAdapter(SavedWeaponsListener {
//                weapon ->
//            findNavController()
//                .navigate(SavedWeaponsFragmentDirections
//                    .actionSavedWeaponsFragmentToViewEditDeleteFragment(weapon.weaponId))
//                        savedWeaponViewModel.onWeaponClicked(weapon)
//
//            Log.i("EDIT3", "$weapon")
//        })

        val savedWeaponAdapter = SavedWeaponsEditDeleteAdapter( this )

        binding.RecyclerViewSavedWeapon.adapter = savedWeaponAdapter
        binding.RecyclerViewSavedWeapon.setOnLongClickListener {
            Log.i("LONG", "Adapter")
            true
        }


        //observe calculations
        savedWeaponViewModel.weapons.observe(viewLifecycleOwner, Observer { weapon ->
            weapon?.let {
                savedWeaponAdapter.list = weapon
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }

    //for short click, navigate to edit
    override fun onItemClick(position: Int, weapon: Component) {
        findNavController()
            .navigate(SavedWeaponsFragmentDirections
                .actionSavedWeaponsFragmentToViewEditDeleteFragment(weapon.weaponId))

        savedWeaponViewModel.onWeaponClicked(weapon)
    }

    //for long click delete
    override fun onItemLongClick(position: Int, weapon: Component) {
        findNavController()
            .navigate(
                SavedWeaponsFragmentDirections
                    .ActionSavedWeaponsFragmentToDeleteWeaponFragment(weapon.weaponId)
            )
    }


}