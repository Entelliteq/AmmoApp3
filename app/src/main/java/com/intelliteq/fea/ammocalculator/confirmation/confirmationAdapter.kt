package com.intelliteq.fea.ammocalculator.confirmation


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelliteq.fea.ammocalculator.R
import kotlinx.android.synthetic.main.ammo_cardview.view.*

class ConfirmAdapter(private  val confirmList: List<Confirm>) : RecyclerView.Adapter<ConfirmAdapter.ConfirmViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ammo_cardview, parent, false)

        return ConfirmViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConfirmViewHolder, position: Int) {
        val currentItem = confirmList[position]

        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2
    }

    override fun getItemCount() = confirmList.size

    class ConfirmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView1:TextView = itemView.text1
        val textView2:TextView = itemView.text2
    }
}