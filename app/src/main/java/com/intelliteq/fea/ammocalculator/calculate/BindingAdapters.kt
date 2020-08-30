package com.intelliteq.fea.ammocalculator.calculate



import android.widget.NumberPicker
import androidx.databinding.BindingAdapter



/**
 * Binding Adapter to set min and max in number pickers.
 */
@BindingAdapter(value = ["app:setMinMaxPicker", "android:max"], requireAll = true)
fun setMinMaxPicker(view: NumberPicker, min: Int, max: Int) {
    view.minValue = min
    view.maxValue = max
    view.wrapSelectorWheel = true
}
//
//@BindingAdapter("app:FEAentries")
//fun Spinner.setEntries(entries: List<Any>?) {
//    setSpinnerEntries(entries)
//}
//
//
//
//object SpinnerExtensions {
//
//    /**
//     * set spinner entries
//     */
//    fun Spinner.setSpinnerEntries(entries: List<Any>?) {
//        if (entries != null) {
//            val arrayAdapter = ArrayAdapter(context, R.layout.simple_spinner_item, entries)
//            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            adapter = arrayAdapter
//        }
//    }
//}