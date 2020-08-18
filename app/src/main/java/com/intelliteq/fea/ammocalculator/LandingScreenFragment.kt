package com.intelliteq.fea.ammocalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.databinding.FragmentLandingScreenBinding


class LandingScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentLandingScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_landing_screen, container, false)
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_landing_screen, container, false)

        binding.newInputButton.setOnClickListener {
            view: View -> view.findNavController().navigate(R.id.action_landingScreenFragment3_to_weaponFragment4)
        }
        return binding.root

    }


}