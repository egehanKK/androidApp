package com.egehankarakose.sorudefterim

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Adapters.OnItemClickListener2
import com.egehankarakose.sorudefterim.Adapters.OnItemClickListener3
import com.egehankarakose.sorudefterim.Adapters.SubjectAdapter
import com.egehankarakose.sorudefterim.Models.SubjectModel
import kotlinx.android.synthetic.main.activity_subject_select.*

class CourseSelectActivity : AppCompatActivity(), OnItemClickListener2 {

    var course = R.array.tyt_courses



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_select)

        subjectRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        var selectedField = ""

        var bundle :Bundle?= intent.extras
        if (bundle != null){
            selectedField = bundle.getString("selectedField")

        }
        if (selectedField.compareTo("AYT") == 0){
            course = R.array.ayt_courses
        }else{
            course = R.array.tyt_courses
        }


        var courseArray = resources.getStringArray(course)

        val dataList = ArrayList<SubjectModel>()
        for (i in 0..(courseArray.size-1)){
            dataList.add(
                SubjectModel(i,
                    courseArray.get((i))
                )
            )
        }


        val subAdapter =
            SubjectAdapter(dataList, this)
//        set the recyclerView to the adapter
        subjectRecyclerView.adapter = subAdapter


    }

    override fun onItemClicked(model: SubjectModel) {
        var intent = Intent()
        intent.putExtra("courseName",model.name)
        intent.putExtra("courseId",model.id)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }
}
