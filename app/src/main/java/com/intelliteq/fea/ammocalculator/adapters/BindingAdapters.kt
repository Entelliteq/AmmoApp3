package com.intelliteq.fea.ammocalculator.adapters



import android.R
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import com.intelliteq.fea.ammocalculator.persistence.models.*


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
fun setSpinnerEntriesFea(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToFEA(entries)
  //  Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
  //  Log.i("Bind", "FEA entries $entries")
}


@BindingAdapter("android:entriesType")
fun setSpinnerEntriesType(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToType(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
  //  Log.i("BIND", "Type $entries")

}

@BindingAdapter("android:entriesDescription")
fun setSpinnerEntriesDescription(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToDescription(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    spinner.onItemSelectedListener
   // Log.i("Bind", "Desc entries $entries")

}

@BindingAdapter("android:entriesAmmoType")
fun setSpinnerEntriesAmmoType(spinner: Spinner, entries: List<Ammo>?) {
    val entriesInt = convertEntriesToAmmoType(entries)
   // Log.i("Error", "Spinner Type/dodic $entries")
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
fun setSpinnerEntriesComponentAmmoType(spinner: Spinner, entries: List<Ammo>?) {

    val entriesInt = convertEntriesToComponentAmmo(entries)
    //Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    Log.i("Weapon ammo", "Ammo type $entries")
}

@BindingAdapter("android:visibleOrGone")
fun View.setVisibility(visible: Boolean) {
    visibility = if(visible) View.VISIBLE else View.GONE
}


@BindingAdapter("android:setString")
fun TextView.setString(item: Int) {
    text = item.toString()
}


fun convertEntriesToFEA(entries: List<Component>?) : List<Int> {
    val feas = mutableListOf<Int>()
    entries?.forEach {
        if(it.FEA_id != 0)  feas.add(it.FEA_id)
    }
    return    feas
}

fun convertEntriesToType(entries: List<Component>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        if(it.FEA_id != 0) type.add(it.componentTypeId)
    }
    return type
}

fun convertEntriesToDescription(entries: List<Component>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        if(it.FEA_id != 0) type.add(it.componentDescription)
    }
    return type
}

fun convertEntriesToAmmoType(entries: List<Ammo>?) : List<String> {

    val type = mutableListOf<String>()
    entries?.forEach {
        type.add(it.ammoDODIC.toString())
      //  Log.i("ERROR1", "${it.ammoDODIC}")

    }
    return type
}

fun convertEntriesToComponent(entries: List<Component>?) : List<String> {
    val type = mutableListOf<String>()
    entries?.forEach {
        type.add(it.componentTypeId)
    }
    return type
}

fun convertEntriesToComponentAmmo(entries: List<Ammo>?) : List<String> {
   // Log.i("ERROR2", "here")
    val type = mutableListOf<String>()
    entries?.forEach {
        type.add(it.ammoDODIC.toString())
    }
    return type
}
