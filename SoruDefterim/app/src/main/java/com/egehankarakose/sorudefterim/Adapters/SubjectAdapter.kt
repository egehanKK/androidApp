package com.egehankarakose.sorudefterim.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Models.SubjectModel
import com.egehankarakose.sorudefterim.R
import kotlinx.android.synthetic.main.adapter_subject_layout.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class MyHolder2(itemView: View): RecyclerView.ViewHolder(itemView){
    val subjectName = itemView.subjectName


    fun bind(model: SubjectModel, clickListener: OnItemClickListener2){
        subjectName.text = model.name


        itemView.setOnClickListener{

            itemView.setBackgroundColor(Color.parseColor("#F8F8FA"))
            Timer("clickEffect", false).schedule(100){
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            clickListener.onItemClicked(model)


        }

    }

}

class SubjectAdapter(val subjectList: ArrayList<SubjectModel>, val itemClickListener: OnItemClickListener2) : RecyclerView.Adapter<MyHolder2>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder2 {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_subject_layout, p0, false)
        return MyHolder2(v);
    }
    override fun getItemCount(): Int {
        return subjectList.size
    }
    override fun onBindViewHolder(myHolder: MyHolder2, position: Int) {
        val subject = subjectList.get(position)
        myHolder.bind(subject,itemClickListener)



    }

}

interface OnItemClickListener2{
    fun onItemClicked(model: SubjectModel)
}
