package com.intelliteq.fea.ammocalculator.savedCalculations

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.R.id.action_savedCalculationsFragment_to_landingScreen
import com.intelliteq.fea.ammocalculator.adapters.SavedCalculationsAdapter
import com.intelliteq.fea.ammocalculator.adapters.SavedCalculationsListener
import com.intelliteq.fea.ammocalculator.calculationOutput.CalculationOutputFragmentArgs
import com.intelliteq.fea.ammocalculator.calculationOutput.CalculationOutputViewModel
import com.intelliteq.fea.ammocalculator.calculationOutput.CalculationOutputViewModelFactory
import com.intelliteq.fea.ammocalculator.databinding.FragmentSavedCalculationsBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase
import kotlinx.android.synthetic.main.fragment_saved_calculations.*


/**
 * A simple [Fragment] subclass.
 * Use the [SavedCalculationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedCalculationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSavedCalculationsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_calculations, container, false)
        // Inflate the layout for this fragment

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val calculation = AmmoRoomDatabase.getAppDatabase(application)!!.calculationDao

        //creating a view model using the factory
        val viewModelFactory = SavedCalculationsViewModelFactory(calculation)

        val savedCalculationViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SavedCalculationsViewModel::class.java)

        binding.savedCalculationsViewModel
        binding.lifecycleOwner = this

        savedCalculationViewModel.naviagteToModifyCalculation.observe(viewLifecycleOwner, Observer {calc ->
            calc?.let {
                Log.i("calc12", "calc: $calc")
                SavedCalculationsFragmentDirections.SavedCalculationsFragmentToModifyCalculationFragment(
                    calc.calculationId, calc.numberOfDays, calc.assaultIntensity, calc.calculationName)
                savedCalculationViewModel.onModifyCalculationNavigated()
            }
        })


        val savedCalcAdapter = SavedCalculationsAdapter(SavedCalculationsListener {
            calc ->
            findNavController().navigate(SavedCalculationsFragmentDirections.SavedCalculationsFragmentToModifyCalculationFragment(
                calc.calculationId, calc.numberOfDays, calc.assaultIntensity, calc.calculationName))
           // Toast.makeText(context, "${calc}", Toast.LENGTH_LONG).show()
            savedCalculationViewModel.onCalculationClicked(calc)

        })

        binding.RecyclerViewSavedCalculations.adapter = savedCalcAdapter


        savedCalculationViewModel.calculations.observe(viewLifecycleOwner, Observer {
            calc ->
            calc?.let {
                savedCalcAdapter.submitList(calc)
            }
        })





        return binding.root
    }



}