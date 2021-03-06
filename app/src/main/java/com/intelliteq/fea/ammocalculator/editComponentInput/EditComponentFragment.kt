package com.intelliteq.fea.ammocalculator.editComponentInput

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditComponentBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_edit_component.*


/**
 * A simple [Fragment] subclass.
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


        //binding
        binding.lifecycleOwner = this
        binding.editComponentViewModel = editComponentViewModel

        //edit component update button
        binding.editComponentUpdateButton.setOnClickListener {
            val type: String = compTypeEdit.text.toString()
            val desc: String = compDescriptionEdit.text.toString()

            if(type.trim().isNotEmpty()) {
                editComponentViewModel.updateType(type)
            }

            if(desc.trim().isNotEmpty() ) {
                editComponentViewModel.updateDescription(desc)
            }

            //destination 1 - validate
            //destination 2 - edit
            if(arguments.destination == 1) {
                it.findNavController()
                    .navigate(
                        EditComponentFragmentDirections
                            .ActionEditComponentFragmentToConfirmation(arguments.weaponKey)
                    )
            } else if(arguments.destination == 2) {
                it.findNavController()
                    .navigate(
                        EditComponentFragmentDirections
                            .ActionEditComponentFragmentToViewEditDeleteFragment(arguments.weaponKey)
                    )
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}