package com.intelliteq.fea.ammocalculator.calculationOutput

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentOutputBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [CalculationOutputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalculationOutputFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentOutputBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_output, container, false)


        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val dataSourceCalculation = AmmoRoomDatabase.getAppDatabase(application)!!.calculationsDao
        val dataSourceSinlge = AmmoRoomDatabase.getAppDatabase(application)!!.singleWeaponCalculationDao
        val arguments = CalculationOutputFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = CalculationOutputViewModelFactory(
            arguments.calculationKey,   dataSourceCalculation, dataSourceSinlge)

        val calculateOutputViewModel = ViewModelProvider(this, viewModelFactory)
            .get(CalculationOutputViewModel::class.java)

        binding.calculationOutputViewModel
        binding.lifecycleOwner = this


        binding.recalculate.setOnClickListener {
                view: View -> view.findNavController()
            .navigate(CalculationOutputFragmentDirections.ActionCalculationOutputScreenToCalculateSelection(-1))
        }

        binding.home.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.landingScreen)
        }

        val adapter = WeaponOutputAdapter()
        binding.RecyclerViewWeapons.adapter = adapter

        calculateOutputViewModel.weapon.observe(viewLifecycleOwner, Observer {
            weapon ->
            weapon?.let {
                adapter.data = weapon
            }
        })

        calculateOutputViewModel.single.observe(viewLifecycleOwner, Observer {
            single ->
            single?.let {
                adapter.quantity = single
            }
        })



        // Inflate the layout for this fragment
        return binding.root
    }



}