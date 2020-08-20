package com.intelliteq.fea.ammocalculator.componentAmmo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.intelliteq.fea.ammocalculator.R


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_component_ammo, container, false)
    }

}