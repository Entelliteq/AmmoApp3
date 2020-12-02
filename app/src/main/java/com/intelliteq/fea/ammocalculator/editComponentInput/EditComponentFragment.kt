package com.intelliteq.fea.ammocalculator.editComponentInput

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditComponentBinding
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditWeaponBinding
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponFragmentArgs
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponViewModel
import com.intelliteq.fea.ammocalculator.editWeaponInput.EditWeaponViewModelFactory
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_component_input.*
import kotlinx.android.synthetic.main.fragment_edit_component.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditComponentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditComponentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEditComponentBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_component, container,false)

        val application = requireNotNull(this.activity).application
        val dataSourceComponent = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
        val arguments = EditComponentFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = EditComponentViewModelFactory(arguments.componentKey, dataSourceComponent)

        val editComponentViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditComponentViewModel::class.java)


        binding.lifecycleOwner = this
        binding.editComponentViewModel = editComponentViewModel



        binding.editComponentUpdateButton.setOnClickListener {
            val type: String = compTypeEdit.text.toString()
            val desc: String = compDescriptionEdit.text.toString()

            if(type.trim().isNotEmpty()) {
                Log.i("edit4", "type: _${compTypeEdit.text}_")
                editComponentViewModel.updateType(type)
            }

            if(desc.trim().isNotEmpty() ) {
                Log.i("edit4", "desc: _${compDescriptionEdit.text}_")
                editComponentViewModel.updateDescription(desc)
            }

            it.findNavController()
                .navigate(EditComponentFragmentDirections
                    .ActionEditComponentFragmentToConfirmation(arguments.weaponKey))
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}