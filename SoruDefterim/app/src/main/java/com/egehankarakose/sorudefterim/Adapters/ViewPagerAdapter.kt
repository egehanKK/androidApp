package com.egehankarakose.sorudefterim.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.egehankarakose.sorudefterim.R


class ViewPagerAdapter(private val context: Context, val imagesArray :ArrayList<Bitmap>) : PagerAdapter(){
    private var layoutInflater: LayoutInflater? = null
    private var images = imagesArray




    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`

    }

    override fun getCount(): Int {
       return images.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v = layoutInflater!!.inflate(R.layout.slider_item_view, null)
        val image = v.findViewById<View>(R.id.image_view) as ImageView
        image.setImageBitmap(images[position])
        val vp = container as ViewPager
        vp.addView(v, 0)

        var contentTitle = v.findViewById<TextView>(R.id.contentTitle)
        if (position == 0){
            contentTitle.text = "Soru"
        }else{
            contentTitle.text = "Çözüm "+ position.toString()
        }
        return v


    }

    override fun destroyItem(container: View, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)

    }

}