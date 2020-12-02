package com.intelliteq.fea.ammocalculator.editAmmoInput

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
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditAmmoBinding
import com.intelliteq.fea.ammocalculator.databinding.FragmentEditComponentBinding
import com.intelliteq.fea.ammocalculator.editComponentInput.EditComponentFragmentArgs
import com.intelliteq.fea.ammocalculator.editComponentInput.EditComponentFragmentDirections
import com.intelliteq.fea.ammocalculator.editComponentInput.EditComponentViewModel
import com.intelliteq.fea.ammocalculator.editComponentInput.EditComponentViewModelFactory
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_edit_ammo.*
import kotlinx.android.synthetic.main.fragment_edit_component.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditAmmoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditAmmoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEditAmmoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_ammo, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSourceComponent = AmmoRoomDatabase.getAppDatabase(application)!!.ammoDao
        val arguments = EditAmmoFragmentArgs.fromBundle(requireArguments()) //change

        //creating a view model using the factory
        val viewModelFactory = EditAmmoViewModelFactory(arguments.ammoKey, dataSourceComponent)

        val editAmmoViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditAmmoViewModel::class.java)


        binding.lifecycleOwner = this
        binding.editAmmoViewModel = editAmmoViewModel

        binding.editAmmoUpdateButton.setOnClickListener {
            val dodic: String = DodicUpdate.text.toString()
            val desc: String = DescriptionUpdate.text.toString()

            if (!TrainingUpdate.text.isNullOrEmpty()) {
                val trainT: Int = Integer.parseInt(TrainingUpdate.text.toString())
                editAmmoViewModel.updateTraining(trainT)
            }

            if (!Sustainupdate.text.isNullOrEmpty()) {
                val sustain: Int = Integer.parseInt(Sustainupdate.text.toString())
                editAmmoViewModel.updateSustain(sustain)
            }

            if (!LightAssaultUpdate.text.isNullOrEmpty()) {
                val light: Int = Integer.parseInt(LightAssaultUpdate.text.toString())
                editAmmoViewModel.updateLight(light)
            }

            if (!MediumAssaultInput.text.isNullOrEmpty()) {
                val medium: Int = Integer.parseInt(MediumAssaultInput.text.toString())
                editAmmoViewModel.updateMedium(medium)
            }

            if (!HeavyAssaultUpdate.text.isNullOrEmpty()) {
                val heavy: Int = Integer.parseInt(HeavyAssaultUpdate.text.toString())
                editAmmoViewModel.updateHeavy(heavy)
            }

            if (!SecurityUpdate.text.isNullOrEmpty()) {
                val secure: Int = Integer.parseInt(SecurityUpdate.text.toString())
                editAmmoViewModel.updateSecurity(secure)
            }

            if (dodic.trim().isNotEmpty()) {
                Log.i("edit4", "type: _${dodic}_")
                editAmmoViewModel.updateDodic(dodic)
            }

            if (desc.trim().isNotEmpty()) {
                Log.i("edit4", "desc: _${desc}_")
                editAmmoViewModel.updateDesc(desc)
            }

            it.findNavController()
                .navigate(EditAmmoFragmentDirections
                    .actionEditAmmoFragToConfirmation(arguments.weaponKey)

                )
        }


        // Inflate the layout for this fragment
        return binding.root
    }


}