package com.intelliteq.fea.ammocalculator.calculate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.component.ComponentFragmentArgs
import com.intelliteq.fea.ammocalculator.component.ComponentViewModel
import com.intelliteq.fea.ammocalculator.component.ComponentViewModelFactory
import com.intelliteq.fea.ammocalculator.databinding.FragmentCalculateBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_calculate.*


class CalculateFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentCalculateBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_calculate, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao

        //creating a view model using the factory
        val viewModelFactory = CalculateViewModelFactory(dataSource, application)
        val calculateViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(CalculateViewModel::class.java)

        //setting the binding values
        binding.calculateViewModel = calculateViewModel
        binding.lifecycleOwner = this

        binding.pickerWeapons.setOnValueChangedListener { picker, oldVal, newVal ->
            calculateViewModel.getWeaponNumber(newVal)
        }

        binding.pickerDays.setOnValueChangedListener { pickerDays, oldVal, newVal ->
            calculateViewModel.getDayNumber(newVal)
        }

        calculateViewModel.weapons.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.spinnerFea
            }
        })


        binding.spinnerFea.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                          val int = parent.getItemAtPosition(position)
                    calculateViewModel.useFea(int as Int)
                    Log.i("Weapon FEA", "$int")
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }



        return binding.root
    }

}