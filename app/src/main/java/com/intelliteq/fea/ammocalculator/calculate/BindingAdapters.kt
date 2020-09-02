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
import com.intelliteq.fea.ammocalculator.persistence.models.Weapon


/**
 * Binding Adapter to set min and max in number pickers.
 */
@BindingAdapter(value = ["app:setMinMaxPicker", "android:max"], requireAll = true)
fun setMinMaxPicker(view: NumberPicker, min: Int, max: Int) {
    view.minValue = min
    view.maxValue = max
    view.wrapSelectorWheel = true
}

@BindingAdapter("android:entriesFea")
fun setSpinnerEntriesFea(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToFEA(entries)
    Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    Log.i("Weapon", "$entries")
}


@BindingAdapter("android:entriesType")
fun setSpinnerEntriesType(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToType(entries)
    Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    Log.i("Weapon", "$entries")

}

@BindingAdapter("android:entriesDescription")
fun setSpinnerEntriesDescription(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToDescription(entries)
    Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    spinner.onItemSelectedListener
    Log.i("Weapon", "$entries")

}

@BindingAdapter("onItemSelected")
fun Spinner.setItemSelectedListener(itemSelectedListener: ItemSelectedListener?) {
    setSpinnerItemSelectedListener(itemSelectedListener)
}

/**
 * set spinner onItemSelectedListener listener
 */
fun Spinner.setSpinnerItemSelectedListener(listener: ItemSelectedListener?) {
    if (listener == null) {
        onItemSelectedListener = null
    } else {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (tag != position) {
                    listener.onItemSelected(parent.getItemAtPosition(position))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}

interface ItemSelectedListener {
    fun onItemSelected(item: Any)
}


fun convertEntriesToFEA(entries: List<Weapon>?) : List<Int> {
    val feas = mutableListOf<Int>()
    val entriesSize = entries?.size
    Log.i("Weapon entries: ", " size $entriesSize")
    entries?.forEach {
        feas.add(it.FEA_id)
    }
    return    feas
}

fun convertEntriesToType(entries: List<Weapon>?) : List<String> {
    val type = mutableListOf<String>()
    val entriesSize = entries?.size
    Log.i("Weapon entries: ", " size $entriesSize")
    entries?.forEach {
        type.add(it.weaponTypeID)
    }
    return type
}

fun convertEntriesToDescription(entries: List<Weapon>?) : List<String> {
    val type = mutableListOf<String>()
    val entriesSize = entries?.size
    Log.i("Weapon entries: ", " size $entriesSize")
    entries?.forEach {
        type.add(it.weaponDescription)
    }
    return type
}
