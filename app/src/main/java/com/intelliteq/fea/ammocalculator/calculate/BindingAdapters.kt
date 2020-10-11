package com.intelliteq.fea.ammocalculator.calculate



import android.R
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.intelliteq.fea.ammocalculator.persistence.models.Component
import com.intelliteq.fea.ammocalculator.persistence.models.ComponentAmmo
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon
import com.intelliteq.fea.ammocalculator.persistence.models.WeaponAmmo


/**
 * Binding Adapter to set min and max in number pickers.
 */
@BindingAdapter(value = ["app:setMinMaxPicker", "android:max", "android:default"], requireAll = true)
fun setMinMaxPicker(view: NumberPicker, min: Int, max: Int, value: Int) {
    view.minValue = min
    view.maxValue = max
    view.value = value
    view.wrapSelectorWheel = true
}

@BindingAdapter("android:entriesFea")
fun setSpinnerEntriesFea(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToFEA(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    //Log.i("Weapon", "$entries")
}


@BindingAdapter("android:entriesType")
fun setSpinnerEntriesType(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToType(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    //Log.i("Weapon", "$entries")

}

@BindingAdapter("android:entriesDescription")
fun setSpinnerEntriesDescription(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToDescription(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    spinner.onItemSelectedListener
    //Log.i("Weapon", "$entries")

}

@BindingAdapter("android:entriesAmmoType")
fun setSpinnerEntriesAmmoType(spinner: Spinner, entries: List<WeaponAmmo>?) {
    val entriesInt = convertEntriesToAmmoType(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    //Log.i("Weapon Ammo", " type $entries")
}

@BindingAdapter("android:entriesCompType")
fun setSpinnerEntriesComponentType(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToComponent(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    Log.i("Weapon Comp", " type $entries")
}

@BindingAdapter("android:entriesCompAmmoType")
fun setSpinnerEntriesComponentAmmoType(spinner: Spinner, entries: List<ComponentAmmo>?) {
    val entriesInt = convertEntriesToComponentAmmo(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    Log.i("Weapon Comp", "Ammo type $entries")
}

@BindingAdapter("android:visibleOrNot")
fun View.setVisibility(visible: Boolean) {
    visibility = if(visible) View.VISIBLE else View.INVISIBLE
}


fun convertEntriesToFEA(entries: List<Weapon>?) : List<Int> {
    val feas = mutableListOf<Int>()
    entries?.forEach {
        if(it.FEA_id != 0)  feas.add(it.FEA_id)
    }
    return    feas
}

fun convertEntriesToType(entries: List<Weapon>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        if(it.FEA_id != 0) type.add(it.weaponTypeID)
    }
    return type
}

fun convertEntriesToDescription(entries: List<Weapon>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        if(it.FEA_id != 0) type.add(it.weaponDescription)
    }
    return type
}

fun convertEntriesToAmmoType(entries: List<WeaponAmmo>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        type.add(it.ammoTypeID.toString())
    }
    return type
}

fun convertEntriesToComponent(entries: List<Component>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        type.add(it.componentTypeID)
    }
    return type
}

fun convertEntriesToComponentAmmo(entries: List<ComponentAmmo>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        type.add(it.ammoTypeID.toString())
    }
    return type
}
