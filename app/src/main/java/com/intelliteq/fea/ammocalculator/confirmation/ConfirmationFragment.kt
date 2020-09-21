package com.intelliteq.fea.ammocalculator.confirmation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.databinding.FragmentConfirmationBinding


/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentConfirmationBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_confirmation, container, false )

        binding.save.setOnClickListener {
                view: View -> view.findNavController().navigate(R.id.confirmation_to_calculate)
        }




        // Inflate the layout for this fragment
        return binding.root
    }

}





//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import com.intelliteq.fea.ammocalculator.confirmation.ConfirmationFragment as ConfirmationFragment

//
///**
// * A simple [Fragment] subclass.
// * Use the [ConfirmationFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class ConfirmationFragment() : Fragment() {
//
//    private var weaponID: Long = 0
//
//    //create coroutine
//    private val viewModelJob = Job()
//    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val binding: FragmentConfirmationBinding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_confirmation, container, false
//        )
//
//        arguments?.let {
//            val args = ConfirmationFragmentArgs.fromBundle(it)
//
//            weaponID = args.weaponKey
//
//        }
//
//        binding.save.setOnClickListener { view: View ->
//            view.findNavController().navigate(R.id.confirmation_to_calculate)
//        }
//
//        binding.edit.setOnClickListener { view: View ->
//            view.findNavController().navigate(R.id.confirmation_to_weapon_input)
//        }
//
//        // Inflate the layout for this fragment
//        return binding.root
//    }

//    //this helps make to view of the confirmations fragment with the recyclerview
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setUpAdapter()
//    }
//
//    //makes the connection between the recyclerview and the fragment
//    private fun setUpAdapter() {
//        uiScope.launch {
//            val confirmList = arrayList()
//
//            confirmView.adapter = ConfirmAdapter(confirmList)
//            confirmView.layoutManager = LinearLayoutManager(context)
//            confirmView.setHasFixedSize(true)
//        }
//    }
//
//    //fills info into cardviews
//
//    private suspend fun connectData(): ConfirmModel {
//        val application = requireNotNull(this.activity).application
//        val database: AmmoRoomDatabase = AmmoRoomDatabase.getAppDatabase(application)!!
//        return ConfirmModel(database, application)
//    }
//
//    private suspend fun arrayList(): List<Confirm> {
//        val list = ArrayList<Confirm>()
//        val dataSource = connectData()//connecting data to fragment
//
//        list += addingWeaponList(dataSource)
//
//        return list
//    }
//
//    private suspend fun addingWeaponList(dataSource:ConfirmModel): List<Confirm> {
//        val list = ArrayList<Confirm>()
//
//        //getting weapon from data
//        val weapon = dataSource.getWeapon(weaponID)
//
//        //weapon info into cardviews
//        if (weapon != null) {
//            list += Confirm("Weapon Type:", weapon.weaponTypeID)//Weapon Type
//            list += Confirm("Weapon Desc:", weapon.weaponDescription)//Weapon Desc
//
//            list += addingWeaponAmmoList(dataSource)
//            list += addingComponentList(dataSource)
//        }
//
//        return list
//    }
//
//    private suspend fun addingWeaponAmmoList(dataSource:ConfirmModel): List<Confirm> {
//        val list = ArrayList<Confirm>()
//
//        //getting weapon Ammo From Data
//        val weaponAmmo = dataSource.getWeaponAmmo(weaponID)
//
//        //weaponAmmo info into cardviews
//        for (element in weaponAmmo) {
//            list += Confirm("Ammo Type:", element.ammoType)
//            list += Confirm("Ammo Desc:", element.ammoDescription)
//            list += Confirm("Ammo DODIC:", element.DODIC)
//            list += Confirm("Training:", element.trainingRate.toString())
//            list += Confirm("Security:", element.securityRate.toString())
//            list += Confirm("Sustain:", element.sustainRate.toString())
//            list += Confirm("Assault, Light:", element.lightAssaultRate.toString())
//            list += Confirm("Assault, Medium:", element.mediumAssaultRate.toString())
//            list += Confirm("Assault, Heavy:", element.heavyAssaultRate.toString())
//        }
//        return list
//    }
//
//    private suspend fun addingComponentList(dataSource:ConfirmModel): List<Confirm> {
//        val list = ArrayList<Confirm>()
//        //getting component From Data
//        val component: List<Component> = dataSource.getComponent(weaponID)
//
//        //component info into cardviews
//        for (compElement in component) {
//            list += Confirm("Comp Type:", compElement.componentTypeID)
//            list += Confirm("Comp Desc:", compElement.componentDescription)
//
//            list += addingComponentAmmoList(dataSource)
//        }
//        return list
//    }
//
//    private suspend fun addingComponentAmmoList(dataSource:ConfirmModel): List<Confirm> {
//        val list = ArrayList<Confirm>()
//        val componentAmmo: List<ComponentAmmo> = dataSource.getComponentAmmo(weaponID)
//        //comp ammo info
//        for(compAmmoElement in componentAmmo)
//        {
//            list += Confirm("Comp Ammo Type:", compAmmoElement.componentAmmoTypeID)
//            list += Confirm("Comp Ammo Desc:", compAmmoElement.componentAmmoDescription)
//            list += Confirm("Comp Ammo DODIC:", compAmmoElement.componentAmmoDODIC)
//        }
//        return list
//    }
//}