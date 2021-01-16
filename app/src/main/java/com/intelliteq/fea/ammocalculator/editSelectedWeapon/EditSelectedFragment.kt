package com.intelliteq.fea.ammocalculator.editSelectedWeapon

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.intelliteq.fea.ammocalculator.R
import com.intelliteq.fea.ammocalculator.adapters.ModifyAmmoListener
import com.intelliteq.fea.ammocalculator.adapters.ModifyWeaponListener
import com.intelliteq.fea.ammocalculator.adapters.ValidateAmmoAdapter
import com.intelliteq.fea.ammocalculator.adapters.ValidateComponentAdapter
import com.intelliteq.fea.ammocalculator.databinding.FragmentSelectedWeaponEditBinding
import com.intelliteq.fea.ammocalculator.persistence.database.AmmoRoomDatabase


class EditSelectedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:  FragmentSelectedWeaponEditBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_selected_weapon_edit, container, false
        )

        //getting the application, arguments set and database
        val application = requireNotNull(this.activity).application
        val ammoDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.ammoDao
        val weaponDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.weaponDao
        val componentDatabase = AmmoRoomDatabase.getAppDatabase(application)!!.componentDao
          val arguments = EditSelectedFragmentArgs.fromBundle(requireArguments())

        //creating a view model using the factory
        val viewModelFactory = EditSelectedViewModelFactory(
            ammoDatabase, componentDatabase, arguments.componentKey )

        val editSelectedWeaponViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditSelectedViewModel::class.java)


        //binding
        binding.editSelectedViewModel
        binding.lifecycleOwner = this

        var weaponKey = 0L
        //Weapon
        editSelectedWeaponViewModel.weapon.observe(viewLifecycleOwner, Observer {
            wep ->
            wep?.let {
                binding.editWpnTypeEdit.text = wep.componentTypeId
                binding.editWpnDescEdit.text = wep.componentDescription
                weaponKey = wep.weaponId
            }
        })

        //destination 1 - validate
        //destination 2 - edit

        //navigate to edit component
        binding.cardViewEditDeleteWeapon.setOnClickListener {
            it.findNavController()
                .navigate(
                    EditSelectedFragmentDirections.ActionViewEditDeleteFragmentToEditWeapon(weaponKey, 2)

                )
        }


        //adapters

        //destination 1 - validate
        //destination 2 - edit
        //weapon ammo
        val weaponAmmoEditAdapter = ValidateAmmoAdapter(ModifyAmmoListener {
            this.requireView().findNavController()
                .navigate(
                    EditSelectedFragmentDirections
                        .ActionViewEditDeleteFragmentToEditAmmoFragment(
                            it.ammoAutoId, it.weaponId, 2))



            Log.i("EDIT5", "$it")
        })

        binding.RecyclerViewAmmoEditDelete.adapter = weaponAmmoEditAdapter

        editSelectedWeaponViewModel.ammosWeapon.observe(viewLifecycleOwner, Observer {
            ammos ->
            ammos?.let {
                weaponAmmoEditAdapter.submitList(ammos)
            }
        })

        //destination 1 - validate
        //destination 2 - edit
        //component ammo
        val componentAmmoEditDeleteAdapter = ValidateAmmoAdapter(ModifyAmmoListener {
            this.requireView().findNavController()
                .navigate(
                    EditSelectedFragmentDirections
                        .ActionViewEditDeleteFragmentToEditAmmoFragment(
                            it.ammoAutoId, it.weaponId, 2))
        })

        binding.RecyclerViewComponentAmmoEditDelete.adapter = componentAmmoEditDeleteAdapter


        //ValidateAmmoAdapter
        editSelectedWeaponViewModel.ammosComp.observe(viewLifecycleOwner, Observer { ammos ->
            ammos?.let {
                componentAmmoEditDeleteAdapter.submitList(ammos)
                if (ammos.isEmpty()) binding.editCompAmmoTextViewEdit.visibility = View.GONE
            }
        })


        //destination 1 - validate
        //destination 2 - edit
        //component
        val componentEditDeleteAdapter = ValidateComponentAdapter(ModifyWeaponListener { weapon ->
            this.requireView().findNavController()
                .navigate(
                    EditSelectedFragmentDirections
                        .actionViewEditDeleteFragmentToEditComponentFragment(
                            weapon.componentAutoId, weapon.weaponId, 2))

        })

        binding.RecyclerViewComponentEditDelete.adapter = componentEditDeleteAdapter

        //ValidateComponentAdapter
        editSelectedWeaponViewModel.components.observe(viewLifecycleOwner, Observer { comps ->
            comps?.let {
                componentEditDeleteAdapter.submitList(comps)
                if (comps.isEmpty()) binding.editComponentTextViewEdit.visibility = View.GONE
            }
        })


        binding.saveEdit.setOnClickListener {
            it.findNavController()
                .navigate(
                    EditSelectedFragmentDirections
                        .ActionViewEditDeleteFragmentToSavedWeaponsFragment()
                )
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    //ADDING
    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    /**
     * RecyclerView: Implementing single item click and long press (Part-II)
     *
     * - creating an innerclass implementing RevyvlerView.OnItemTouchListener
     * - Pass clickListener interface as parameter
     */
    internal class RecyclerTouchListener(
        context: Context?,
        recycleView: RecyclerView,
        private val clicklistener: ClickListener?
    ) :
        OnItemTouchListener {
        private val gestureDetector: GestureDetector
        override fun onInterceptTouchEvent(
            rv: RecyclerView,
            e: MotionEvent
        ): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

        init {
            gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child =
                        recycleView.findChildViewUnder(e.x, e.y)
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child))
                    }
                }
            })
        }
    }


}