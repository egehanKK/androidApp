package com.egehankarakose.sorudefterim

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Adapters.BitmapImageAdapter

import com.egehankarakose.sorudefterim.Adapters.ImagesAdapter
import com.egehankarakose.sorudefterim.Adapters.OnItemClickListener3
import com.egehankarakose.sorudefterim.Models.BitmapImage

import com.egehankarakose.sorudefterim.Models.ImagesModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream


class ShowImagesFragment : Fragment(), OnItemClickListener3 {


    var courseImages:ArrayList<Bitmap> = ArrayList()
    var setIds : ArrayList<String> = ArrayList()
    var courseId = 0
    var courseName = ""
    var subjectName = ""
    var selectedField = ""



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_show_images, container, false)

        var courseImageView = view.findViewById<ImageView>(R.id.courseImageView)
        var courseNameText = view.findViewById<TextView>(R.id.courseNameText)
        var subjectNameText = view.findViewById<TextView>(R.id.subjectNameText)

        var photosRecyclerView =view.findViewById<RecyclerView>(R.id.PhotosRecyclerView)

        var questionNo = view.findViewById<TextView>(R.id.questionNo)



        val bundle :Bundle ?=arguments
        if (bundle!=null){

            courseName = bundle.getString("courseName")
            courseId = bundle.getInt("courseId")
            subjectName = bundle.getString("subjectName")
            selectedField = bundle.getString("selectedField")

        }

        var courseImagesForProfile = arrayOf(
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


        courseImageView.setImageResource(courseImagesForProfile.get(courseId))
        courseNameText.text = courseName

        var resultSubjectName = subjectName
        if (resultSubjectName.length > 16){
            resultSubjectName = resultSubjectName.take(16) + "..."
        }
        subjectNameText.text = resultSubjectName


        photosRecyclerView.layoutManager = GridLayoutManager(view.context, 3) as RecyclerView.LayoutManager?
//      Create an arraylist

        var tableName = courseName.replace("\\s".toRegex(),"") +"_" + subjectName.replace("\\s".toRegex(),"")
        this.getFromDatabase( tableName)

        val dataList = ArrayList<ImagesModel>()
        for (i in 0..courseImages.size-1){
            dataList.add(ImagesModel(courseImages[i],setIds[i]))
        }

//        pass the values to RvAdapter
        val rvAdapter = ImagesAdapter(dataList, this)
//        set the recyclerView to the adapter
        photosRecyclerView.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

        questionNo.text = courseImages.size.toString()





        return view
    }

    override fun onItemClicked(imagesModel: ImagesModel) {

        var intent = Intent(view?.context,ShowSelectedImagesActivity::class.java)
        intent.putExtra("setId",imagesModel.setId)
        intent.putExtra("courseName", courseName)
        intent.putExtra("subjectName",subjectName)
        intent.putExtra("selectedField",selectedField)
        startActivityForResult(intent,1)

    }



    fun getFromDatabase(tableName : String){

        try {

            val database = activity?.openOrCreateDatabase(selectedField, Context.MODE_PRIVATE,null)

            val cursor = database?.rawQuery("SELECT * FROM ($tableName)",null)
            val noteIx = cursor?.getColumnIndex("note")
            val idIx = cursor?.getColumnIndex("id")
            val image = cursor!!.getColumnIndex("image")
            val setId = cursor!!.getColumnIndex("setId")

            while (cursor!!.moveToNext()) {

                if (cursor.getString(setId) !in setIds ){
                    val byteArray = cursor.getBlob(image)
                    val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                    courseImages.add(bitmap)

                }
                setIds.add(cursor.getString(setId))

            }

            cursor.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){

                parentFragmentManager.popBackStack()
                var fm = parentFragmentManager.beginTransaction()
                val bundle2 = Bundle()
                bundle2.putString("courseName",courseName)
                bundle2.putString("subjectName",subjectName)
                if (courseId != null) {
                    bundle2.putInt("courseId",courseId)
                }
                bundle2.putString("selectedField",selectedField)
                var frag = ShowImagesFragment()
                frag.arguments = bundle2
                fm.replace(R.id.inner_fragment,frag)
                fm.addToBackStack(null)
                fm.commit()


            }
        }
    }


}
