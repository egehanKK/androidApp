package com.egehankarakose.sorudefterim

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Adapters.OnItemClickListener
import com.egehankarakose.sorudefterim.Adapters.RvAdapter
import com.egehankarakose.sorudefterim.Models.MainModel

class ShowCoursesFragment : Fragment(),OnItemClickListener {

    var courseName = ""
    var courseId = -1
    var subjectName = ""
    var fieldName = "TYT"


  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
      val view = inflater.inflate(R.layout.activity_second_main, container, false)



      var btnSwitch = view.findViewById<Switch>(R.id.selectFieldSwitch)




      val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
//        Initializing the type of layout, here I have used LinearLayoutManager you can try GridLayoutManager
//        Based on your requirement to allow vertical or horizontal scroll , you can change it in  LinearLayout.VERTICAL


      recyclerView?.layoutManager = GridLayoutManager(view.context, 2) as RecyclerView.LayoutManager?
//        Create an arraylist



      btnSwitch.text = fieldName + "  "




      var courseImages = arrayOf(
          R.mipmap.math,
          R.mipmap.lit,
          R.mipmap.trigo,
          R.mipmap.physics,
          R.mipmap.chem,
          R.mipmap.bio,
          R.mipmap.history,
          R.mipmap.geo,
          R.mipmap.philo,
          R.mipmap.religion)

      var courses = resources.getStringArray(R.array.tyt_courses)

      if (fieldName.compareTo("TYT") == 0){
          courses = resources.getStringArray(R.array.tyt_courses)

      }else if (fieldName.compareTo("AYT") == 0){
          courses = resources.getStringArray(R.array.ayt_courses)
      }

      loadView(view,courses,courseImages)


      btnSwitch.setOnClickListener {
          if (btnSwitch.isChecked) {

              fieldName = "AYT"
              btnSwitch.text = fieldName + "  "

              courses = resources.getStringArray(R.array.ayt_courses)


              loadView(view,courses,courseImages)




          } else {
              btnSwitch.text = "TYT  "
              btnSwitch.text = fieldName + "  "
              courses = resources.getStringArray(R.array.tyt_courses)


              loadView(view,courses,courseImages)

          }
      }




      return view
  }

    override fun onItemClicked(model: MainModel) {
        val intent = Intent(view?.context,subjectSelectActivity::class.java)
        intent.putExtra("courseId", model.id)
        intent.putExtra("courseName", model.name)
        intent.putExtra("fieldName", fieldName)
        startActivityForResult(intent,1)


    }

    fun loadView(view: View, courses: kotlin.Array<String>, courseImages: kotlin.Array<Int>){


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
//        Initializing the type of layout, here I have used LinearLayoutManager you can try GridLayoutManager
//        Based on your requirement to allow vertical or horizontal scroll , you can change it in  LinearLayout.VERTICAL


        recyclerView?.layoutManager = GridLayoutManager(view.context, 2) as RecyclerView.LayoutManager?
//        Create an arraylist



        val dataList = ArrayList<MainModel>()
        for (i in 0..(courses.size-1)){
            dataList.add(
                MainModel(
                    i,
                    courses.get(i),
                    courseImages.get(i)
                )
            )
        }

//        pass the values to RvAdapter
        val rvAdapter =
            RvAdapter(dataList, this)
//        set the recyclerView to the adapter
        recyclerView.adapter = rvAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                courseName = data!!.getStringExtra("courseName")
                courseId = data!!.getIntExtra("courseId",-1)
                subjectName = data!!.getStringExtra("subjectName")

                var fm = parentFragmentManager.beginTransaction()
                val bundle2 = Bundle()
                bundle2.putString("courseName",courseName)
                bundle2.putString("subjectName",subjectName)
                if (courseId != null) {
                    bundle2.putInt("courseId",courseId)
                }
                bundle2.putString("selectedField",fieldName)
                var frag = ShowImagesFragment()
                frag.arguments = bundle2
                fm.replace(R.id.inner_fragment,frag)
                fm.addToBackStack(null)
                fm.commit()


            }
        }
    }


}