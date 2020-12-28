package com.intelliteq.fea.ammocalculator.editWeaponInput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditWeaponBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_edit_weapon.*


/**
 * A simple [Fragment] subclass.
 */
class EditWeaponFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditWeaponBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_weapon, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSourceComponent = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val arguments = EditWeaponFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = EditWeaponViewModelFactory(arguments.weaponKey, dataSourceComponent)

        val editWeaponViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditWeaponViewModel::class.java)


        //binding
        binding.lifecycleOwner = this
        binding.editWeaponViewModel = editWeaponViewModel

        //edit update weapon button
        binding.editWeaponUpdateButton.setOnClickListener {
            val type: String = weaponTypeEditComp.text.toString()
            val desc: String = weaponDescriptionEditComp.text.toString()

            if (type.trim().isNotEmpty()) {
                editWeaponViewModel.updateType(type)
            }

            if (desc.trim().isNotEmpty()) {
                editWeaponViewModel.updateDescription(desc)
            }

            it.findNavController()
                .navigate(EditWeaponFragmentDirections
                    .actionEditWeaponToConfirmation2(arguments.weaponKey))
        }

        return binding.root
    }


}