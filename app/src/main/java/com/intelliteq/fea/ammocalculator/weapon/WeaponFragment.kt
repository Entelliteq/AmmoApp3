package com.intelliteq.fea.ammocalculator.weapon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentWeaponBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [WeaponFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeaponFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        //binding object to remove all findByViews
        val binding: FragmentWeaponBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_weapon, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao

        val viewModelFactory = WeaponViewModelFactory(dataSource, application)

        val weaponViewModel = ViewModelProvider(this, viewModelFactory)
            .get(WeaponViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.weaponViewModel = weaponViewModel

        weaponViewModel.navigateToInputWeaponAmmo.observe(
            viewLifecycleOwner,
            Observer { weapon ->
                weapon?.let {
                    this.findNavController()
                        .navigate(WeaponFragmentDirections.WeaponInputToAmmoInput(weapon.weaponAutoId))
                    weaponViewModel.doneNavigation()
                }

        })

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_weapon, container, false)
        return binding.root
    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
//        view.findViewById<Button>(R.id.input_ammo).setOnClickListener(this)
//    }
//
//    override fun onClick(v: View?) {
//        when(v!!.id) {
//            R.id.input_ammo -> {
//                val bundle = bundleOf("inputWeaponRecipient" to R.id.textInputLayout_weapon_type.toString(),
//                    "inputDescRecipient" to R.id.textInputLayout2.toString())
//                navController!!.navigate(R.id.action_fragInputWeapon_to_fragInputAmmo, bundle)
//            }
//        }
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment WeaponFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            WeaponFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}