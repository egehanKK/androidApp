package com.egehankarakose.sorudefterim

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Adapters.OnItemClickListener2
import com.egehankarakose.sorudefterim.Adapters.SubjectAdapter
import com.egehankarakose.sorudefterim.Models.SubjectModel
import kotlinx.android.synthetic.main.activity_subject_select.*

class subjectSelectActivity : AppCompatActivity(),
    OnItemClickListener2 {
    var courseId = 0
    var courseName = ""
    var fieldName = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        var courseArray = arrayOf(R.array.tyt_math,R.array.tyt_lit,R.array.tyt_trigo,R.array.tyt_physics,R.array.tyt_chem,R.array.tyt_bio, R.array.tyt_history, R.array.tyt_geo,R.array.tyt_philo,R.array.tyt_religion)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_select)

        subjectRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)



        val bundle : Bundle?=intent.extras
        if (bundle!=null){
            courseId = bundle.getInt("courseId")
            courseName = bundle.getString("courseName")
            fieldName = bundle.getString("fieldName")



        }



        if (fieldName.compareTo("TYT") == 0){
            courseArray = arrayOf(R.array.tyt_math,R.array.tyt_lit,R.array.tyt_trigo,R.array.tyt_physics,R.array.tyt_chem,R.array.tyt_bio, R.array.tyt_history, R.array.tyt_geo,R.array.tyt_philo,R.array.tyt_religion)


        }
        else if(fieldName.compareTo("AYT") == 0){
            courseArray = arrayOf(R.array.ayt_math,R.array.ayt_lit,R.array.ayt_tri, R.array.ayt_physic, R.array.ayt_chem,R.array.ayt_bio,R.array.aty_history,R.array.ayt_geo,R.array.ayt_philo,R.array.ayt_religion)

        }



        var subjectArray = resources.getStringArray(courseArray.get(courseId))
        val dataList = ArrayList<SubjectModel>()
        for (i in 0..(subjectArray.size-1)){
            dataList.add(
                SubjectModel(i,
                    subjectArray.get((i))
                )
            )
        }

        var courseNameText = findViewById<TextView>(R.id.subjectCourseNameText)
        courseNameText.text = courseName


        val subAdapter =
            SubjectAdapter(dataList, this)
//        set the recyclerView to the adapter
        subjectRecyclerView.adapter = subAdapter


    }

    override fun onItemClicked(model: SubjectModel) {
       var intent = Intent()
        intent.putExtra("courseName",courseName )
        intent.putExtra("subjectName", model.name)
        intent.putExtra("courseId",courseId)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }
}
