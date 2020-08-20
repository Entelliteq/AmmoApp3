package com.intelliteq.fea.ammocalculator.confirmation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentConfirmationBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentConfirmationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_confirmation, container, false )

        binding.save.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.confirmation_to_calculate)
        }

        binding.edit.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.confirmation_to_weapon_input)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

}