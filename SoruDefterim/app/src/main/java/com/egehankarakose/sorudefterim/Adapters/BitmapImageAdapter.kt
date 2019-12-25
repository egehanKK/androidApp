package com.egehankarakose.sorudefterim.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Models.BitmapImage
import com.egehankarakose.sorudefterim.Models.ImagesModel
import com.egehankarakose.sorudefterim.R
import kotlinx.android.synthetic.main.adapter_show_images.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class MyHolder4(itemView: View): RecyclerView.ViewHolder(itemView){
    val image = itemView.addQuestionImage


    fun bind(imagesModel: BitmapImage, clickListener: OnItemClickListener4){
        image.setImageBitmap(imagesModel.image)


        itemView.setOnClickListener{
            itemView.setBackgroundColor(Color.parseColor("#F8F8FA"))
            Timer("clickEffect", false).schedule(100){
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            clickListener.onItemClicked(imagesModel)


        }

    }

}

class BitmapImageAdapter(val userList: ArrayList<BitmapImage>, val itemClickListener: OnItemClickListener4) : RecyclerView.Adapter<MyHolder4>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder4 {
        val v = LayoutInflater.from(p0?.context).inflate(R.layout.adapter_show_images, p0, false)
        v.layoutParams.height = 130
        v.layoutParams.width = 130
        return MyHolder4(v);
    }
    override fun getItemCount(): Int {
        return userList.size
    }
    override fun onBindViewHolder(myHolder: MyHolder4, position: Int) {
        val user = userList.get(position)
        myHolder.bind(user,itemClickListener)



    }

}

interface OnItemClickListener4{
    fun onItemClicked(imagesModel: BitmapImage)
}