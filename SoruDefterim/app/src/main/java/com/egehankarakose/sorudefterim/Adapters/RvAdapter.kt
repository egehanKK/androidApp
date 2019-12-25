package com.egehankarakose.sorudefterim.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Models.MainModel
import com.egehankarakose.sorudefterim.R
import kotlinx.android.synthetic.main.adapter_item_layout.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val name = itemView.tvName
    val count = itemView.tvImage

    fun bind(model: MainModel, clickListener: OnItemClickListener){
        name.text = model.name
        count.setImageResource(model.image)

        itemView.setOnClickListener{
            itemView.setBackgroundColor(Color.parseColor("#F8F8FA"))
            Timer("clickEffect", false).schedule(100){
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            clickListener.onItemClicked(model)


        }

    }



}

class RvAdapter(val userList: ArrayList<MainModel>, val itemClickListener: OnItemClickListener) : RecyclerView.Adapter<MyHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_item_layout, p0, false)
        return MyHolder(v);
    }
    override fun getItemCount(): Int {
        return userList.size
    }
    override fun onBindViewHolder(myHolder: MyHolder, position: Int) {
        val user = userList.get(position)
        myHolder.bind(user,itemClickListener)



    }

}

interface OnItemClickListener{
    fun onItemClicked(model: MainModel)
}