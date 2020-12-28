package com.intelliteq.fea.ammocalculator.adapters



import android.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.intelliteq.fea.ammocalculator.persistence.models.Ammo
import com.intelliteq.fea.ammocalculator.persistence.models.Component


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
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
}


@BindingAdapter("android:entriesType")
fun setSpinnerEntriesType(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToType(entries)
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
}

@BindingAdapter("android:entriesDescription")
fun setSpinnerEntriesDescription(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToDescription(entries)
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    spinner.onItemSelectedListener

}



@BindingAdapter("android:entriesAmmoType")
fun setSpinnerEntriesAmmoType(spinner: Spinner, entries: List<Ammo>?) {
    val entriesInt = convertEntriesToAmmoType(entries)
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
}

@BindingAdapter("android:entriesCompType")
fun setSpinnerEntriesComponentType(spinner: Spinner, entries: List<Component>?) {
    val entriesInt = convertEntriesToComponent(entries)
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
}

@BindingAdapter("android:entriesCompAmmoType")
fun setSpinnerEntriesComponentAmmoType(spinner: Spinner, entries: List<Ammo>?) {
    val entriesInt = convertEntriesToComponentAmmo(entries)
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
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
