package com.intelliteq.fea.ammocalculator.calculate



import android.R
import android.content.res.Resources
import android.util.Log
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

@BindingAdapter("android:entries")
fun setSpinnerEntries(spinner: Spinner, entries: List<Weapon>?) {
    val entriesInt = convertEntriesToFEA(entries)
    Log.i("Weapon Bind", "$entries")
    val arrayAdapter = ArrayAdapter(spinner.context, R.layout.simple_spinner_item, entriesInt)
    arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    spinner.adapter = arrayAdapter
    Log.i("Weapon", "$entries")

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
