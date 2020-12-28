package com.intelliteq.fea.ammocalculator.calculationOutput

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.AmmoOutputAdapter
import com.intelliteq.fea.ammocalculator.adapters.WeaponOutputAdapter
import com.intelliteq.fea.ammocalculator.databinding.FragmentOutputBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 */
class CalculationOutputFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //binding variable and inflating the fragment
        val binding: FragmentOutputBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_output, container, false
        )


        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application

        val dataSourceCalculation = AmmoRoomDatabase.getAppDatabase(application)!!.calculationDao
        val dataSourceSingle =
            AmmoRoomDatabase.getAppDatabase(application)!!.perWeaponCalculationDao
        val arguments = CalculationOutputFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = CalculationOutputViewModelFactory(
            arguments.calculationKey,
            dataSourceCalculation,
            dataSourceSingle,
            arguments.days,
            arguments.intensity
        )

        val calculateOutputViewModel = ViewModelProvider(this, viewModelFactory)
            .get(CalculationOutputViewModel::class.java)

        //binding
        binding.calcOutputViewModel
        binding.lifecycleOwner = this
        binding.typeOutputYeah.text = arguments.intensity
        binding.daysOutput.text = arguments.days.toString()

        //recalculate button
        binding.recalculate.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    CalculationOutputFragmentDirections.ActionCalculationOutputScreenToCalculateSelection(
                        -1
                    )
                )
        }

        //home button
        binding.home.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.landingScreen)
        }

        //adapters
        val weaponAdapter =
            WeaponOutputAdapter()
        binding.RecyclerViewWeapons.adapter = weaponAdapter

        val ammoAdapter =
            AmmoOutputAdapter()
        binding.RecyclerViewAmmo.adapter = ammoAdapter

        calculateOutputViewModel.weapon.observe(viewLifecycleOwner, Observer { weapon ->
            weapon?.let {
                weaponAdapter.data = weapon
            }
        })

        calculateOutputViewModel.cards.observe(viewLifecycleOwner, Observer {
            ammoAdapter.cards = it
        })

        calculateOutputViewModel.perWeaponCalcUsed.observe(viewLifecycleOwner, Observer { single ->
            single?.let {
                weaponAdapter.quantity = single //returns correct for weapon
            }
        })


        //share button
        binding.share.setOnClickListener {
            // calculateOutputViewModel.share(it)
            val text = calculateOutputViewModel.shareTextValue
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text.value)
            startActivity(intent)
        }

        //Dialog Box
        binding.save.setOnClickListener {
            //    Log.i("SAVE","save clicked")
            val alertBuilder = AlertDialog.Builder(this.context, 3)
            val inflater2 = layoutInflater

            val dialogLayout = inflater2.inflate(R.layout.save_edit_text_layout, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.saveAsEditText)
            val text = calculateOutputViewModel.shareTextValue.value

            with(alertBuilder) {
                setTitle("Save This Calculation:").setPositiveButton("Save") { dialog, _ ->
                    dialog.dismiss()
                    calculateOutputViewModel.calculationName.value = editText.text.toString()
                    calculateOutputViewModel.saveNameFromDialog(editText.text.toString(), text.toString())
                }

                setNegativeButton("Cancel") { dialog, _ ->
                    Toast.makeText(this.context, "You Canceled The Save", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                setView(dialogLayout)
                show()
            }
        }


        // Inflate the layout for this fragment
        return binding.root
    }


}

