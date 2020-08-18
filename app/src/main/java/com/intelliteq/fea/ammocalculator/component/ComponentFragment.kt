package com.intelliteq.fea.ammocalculator.component

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentComponentBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


/**
 * A simple [Fragment] subclass.
 * Use the [ComponentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComponentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val binding : FragmentComponentBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_component, container, false )

        val application = requireNotNull(this.activity).application
        val arguments = ComponentFragmentArgs.fromBundle(arguments)

        val dataSource = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao

        val viewModelFactory = ComponentViewModelFactory(arguments.weaponKey, dataSource)

        val componentViewModel =
            ViewModelProvider(this, viewModelFactory)
                .get(ComponentViewModel::class.java)

        binding.lifecycleOwner = this
        binding.componentViewModel = componentViewModel

        componentViewModel.navigateToAnotherComponent.observe(
            viewLifecycleOwner,
            Observer { weaponAmmo ->
                weaponAmmo?.let {
                    this.findNavController()
                        .navigate(ComponentFragmentDirections.ActionComponentFragment2ToComponentAmmoFragment())
                }
            }
        )


        // Inflate the layout for this fragment
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
////        view.findViewById<Button>(R.id.button).setOnClickListener(this)
//        view.findViewById<Button>(R.id.button2).setOnClickListener(this)
//        view.findViewById<Button>(R.id.verify).setOnClickListener(this)
  //  }
//
//    override fun onClick(v: View?) {
//        when(v!!.id) {
////            R.id.add_another_ammo -> {
////                navController!!.navigate(R.id.action_fragInputAmmo_to_fragInputAmmoComp)
////            }
////            R.id.add_component -> {
////                navController!!.navigate(R.id.action_fragInputAmmo_to_fragInputComponent)
////            }
////            R.id.verify -> {
////                navController!!.navigate(R.id.action_fragInputAmmo_to_fragConfirmInfo)
////            }
//        }
//    }
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment ComponentFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            ComponentFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}