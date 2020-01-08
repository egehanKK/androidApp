package com.egehankarakose.sorudefterim.Adapters

import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Models.ImagesModel
import com.egehankarakose.sorudefterim.R
import kotlinx.android.synthetic.main.adapter_show_images.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class MyHolder3(itemView: View): RecyclerView.ViewHolder(itemView){
    val image = itemView.addQuestionImage
    val checkImage = itemView.checkImagesView




    fun bind(imagesModel: ImagesModel, clickListener: OnItemClickListener3){
        image.setImageBitmap(imagesModel.image)
        if (imagesModel.isChecked){
            checkImage.setBackgroundResource(R.drawable.check_icon)
        }else{
            checkImage.setBackgroundResource(R.mipmap.question_mark_icon)
        }


        itemView.setOnClickListener{
            itemView.setBackgroundColor(Color.parseColor("#F8F8FA"))
            Timer("clickEffect", false).schedule(100){
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            clickListener.onItemClicked(imagesModel)


        }

    }

}

class ImagesAdapter(val userList: ArrayList<ImagesModel>, val itemClickListener: OnItemClickListener3) : RecyclerView.Adapter<MyHolder3>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder3 {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_show_images, p0, false)

        return MyHolder3(v)
    }
    override fun getItemCount(): Int {
        return userList.size
    }
    override fun onBindViewHolder(myHolder: MyHolder3, position: Int) {
        val user = userList.get(position)
        myHolder.bind(user,itemClickListener)



    }

}

interface OnItemClickListener3{
    fun onItemClicked(imagesModel: ImagesModel)
}