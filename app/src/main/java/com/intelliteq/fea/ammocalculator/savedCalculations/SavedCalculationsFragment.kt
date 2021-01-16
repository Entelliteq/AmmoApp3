package com.intelliteq.fea.ammocalculator.savedCalculations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.SavedCalculationsAdapter
import com.intelliteq.fea.ammocalculator.adapters.SavedCalculationsListener
import com.intelliteq.fea.ammocalculator.databinding.FragmentSavedCalculationsBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 */
class SavedCalculationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentSavedCalculationsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_calculations, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val calculation = AmmoRoomDatabase.getAppDatabase(application)!!.calculationDao

        //creating a view model using the factory
        val viewModelFactory = SavedCalculationsViewModelFactory(calculation)

        val savedCalculationViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SavedCalculationsViewModel::class.java)

        //binding
        binding.savedCalculationsViewModel
        binding.lifecycleOwner = this

        //navigate to modify screen
        savedCalculationViewModel.naviagteToModifyCalculation.observe(
            viewLifecycleOwner,
            Observer { calc ->
                calc?.let {
                    SavedCalculationsFragmentDirections.SavedCalculationsFragmentToModifyCalculationFragment(
                        calc.calculationId,
                        calc.numberOfDays,
                        calc.assaultIntensity,
                        calc.calculationName
                    )
                    savedCalculationViewModel.onModifyCalculationNavigated()
                }
            })

        //adapter
        val savedCalcAdapter = SavedCalculationsAdapter(SavedCalculationsListener { calc ->
            findNavController().navigate(
                SavedCalculationsFragmentDirections
                    .SavedCalculationsFragmentToModifyCalculationFragment(
                        calc.calculationId,
                        calc.numberOfDays,
                        calc.assaultIntensity,
                        calc.calculationName
                    )
            )
            savedCalculationViewModel.onCalculationClicked(calc)
        })

        binding.RecyclerViewSavedCalculations.adapter = savedCalcAdapter

        //observe calculations
        savedCalculationViewModel.calculations.observe(viewLifecycleOwner, Observer { calc ->
            calc?.let {
                savedCalcAdapter.submitList(calc)
            }
        })


        return binding.root
    }


}