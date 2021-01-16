package com.intelliteq.fea.ammocalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.databinding.FragmentLandingScreenBinding


/**
 * Fragment to move around the app
 */
class LandingScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLandingScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_landing_screen, container, false
        )

        binding.newInputButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.landing_to_weapon_input)
        }

        binding.newCalculationButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(LandingScreenFragmentDirections.ActionLandingScreenToCalculateSelection(-1))
        }

        binding.savedCalculationButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(LandingScreenFragmentDirections.ActionLandingScreenToSavedCalculationsFragment())
        }

        binding.editDeleteButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(LandingScreenFragmentDirections.ActionLandingScreenToSavedWeaponsFragment())
        }
        // Inflate the layout for this fragment
        return binding.root

    }


}