package com.intelliteq.fea.ammocalculator.calculate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.component.ComponentFragmentArgs
import com.intelliteq.fea.ammocalculator.component.ComponentViewModel
import com.intelliteq.fea.ammocalculator.component.ComponentViewModelFactory
import com.intelliteq.fea.ammocalculator.databinding.FragmentCalculateBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_calculate.*


class CalculateFragment : Fragment() {


    private val MIN_DAYS = 1
    private val MAX_DAYS = 21
    private val MIN_WEAPONS = 1
    private val MAX_WEAPONS = 99

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentCalculateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_calculate, container, false)

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao

        //creating a view model using the factory
        val viewModelFactory = CalculateViewModelFactory(dataSource)
        val calculateViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(CalculateViewModel::class.java)

        //setting the binding values
        binding.calculateViewModel = calculateViewModel
        binding.lifecycleOwner = this


        //pickers min and max
        binding.pickerDays.minValue = MIN_DAYS
        binding.pickerDays.maxValue = MAX_DAYS
        binding.pickerDays.wrapSelectorWheel = true

        binding.pickerWeapons.minValue = MIN_WEAPONS
        binding.pickerWeapons.maxValue = MAX_WEAPONS
        binding.pickerWeapons.wrapSelectorWheel = true


        return binding.root
    }

}