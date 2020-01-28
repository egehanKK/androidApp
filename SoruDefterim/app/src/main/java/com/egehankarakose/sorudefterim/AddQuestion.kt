package com.egehankarakose.sorudefterim

import android.app.Activity
import android.content.Intent
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egehankarakose.sorudefterim.Adapters.BitmapImageAdapter
import com.egehankarakose.sorudefterim.Adapters.OnItemClickListener4
import com.egehankarakose.sorudefterim.Models.BitmapImage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


class AddQuestion: Fragment() ,OnItemClickListener4{

    var subjectName = ""
    var courseName = ""
    var courseId = -1
    var imageUri :ArrayList<Bitmap> = ArrayList()
    var note = ""
    var selectedField = "TYT"


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        var view = inflater.inflate(R.layout.fragment_add_question, container, false)

        var selectCourse = view.findViewById<Button>(R.id.selectCourseBtn)
        var selectSubject = view.findViewById<Button>(R.id.selectSubjectBtn)

        var line = view.findViewById<View>(R.id.view2)
        var line2 = view.findViewById<View>(R.id.view3)

        var qImage = view.findViewById<ImageView>(R.id.addQuestionImage)
        var aImage = view.findViewById<ImageView>(R.id.solutionImage)

        var questionImage = view.findViewById<ImageView>(R.id.addQuestionImage)
        var solutionImage = view.findViewById<ImageView>(R.id.solutionImage)

        var addQuestionBtn = view.findViewById<Button>(R.id.addQuestionButton)
        var notesText = view.findViewById<EditText>(R.id.QuestionNotes)

        var btnSwitch = view.findViewById<Switch>(R.id.addQuestionSwitch)


        btnSwitch.setOnClickListener {
            if (btnSwitch.isChecked){
                selectedField = "AYT"
                btnSwitch.text = "AYT  "

            }else{
                selectedField = "TYT"
                btnSwitch.text = "TYT  "
            }
        }

        line.isVisible = false
        line2.isVisible = false
        aImage.isEnabled = false
        selectSubject.isEnabled = false

        selectCourse.setOnClickListener {
            subjectName = ""
            selectSubject.text = "Konu Seç"
            selectSubject.isEnabled = false
            var intent = Intent(view.context,CourseSelectActivity::class.java)
            intent.putExtra("selectedField", selectedField)
            startActivityForResult(intent,2)
        }

        selectSubject.setOnClickListener {
            val intent = Intent(view?.context,subjectSelectActivity::class.java)
            intent.putExtra("courseId", courseId)
            intent.putExtra("courseName", courseName)
            intent.putExtra("fieldName",selectedField)
            startActivityForResult(intent,3)
        }



        questionImage.setImageResource(R.mipmap.add_que_foreground)


        solutionImage.setImageResource(R.mipmap.add_ans_foreground)




        qImage.setOnClickListener{

                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(context!!,this)


        }


        aImage.setOnClickListener {


                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(context!!,this)




        }


        addQuestionBtn.setOnClickListener {

            note = notesText.text.toString()
            var check = saveImage()

            if (check){
                Toast.makeText(context, "Soru Eklendi",Toast.LENGTH_SHORT).show()
                subjectName = ""
                courseName = ""
                selectSubject.text = "Konu Seç"
                selectSubject.isEnabled = false
                qImage?.isEnabled = true
                aImage?.isEnabled = false
                selectCourse.text = "Ders Seç"
                imageUri.clear()
                note = ""
                notesText.text.clear()
                qImage.setImageResource(R.mipmap.add_que_foreground)

                val recyclerView = view?.findViewById<RecyclerView>(R.id.addQuestionRecyclerView)
                recyclerView?.layoutManager = GridLayoutManager(view?.context, 8) as RecyclerView.LayoutManager?

                val dataList = ArrayList<BitmapImage>()


                var line = view?.findViewById<View>(R.id.view2)
                var line2 = view?.findViewById<View>(R.id.view3)
                line?.isVisible = false
                line2?.isVisible = false
//        pass the values to RvAdapter
                val rvAdapter =
                    BitmapImageAdapter(dataList, this)
//        set the recyclerView to the adapter
                recyclerView?.adapter = rvAdapter
            }

        }



        return view


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var selectCourse = view?.findViewById<Button>(R.id.selectCourseBtn)
        var selectSubject = view?.findViewById<Button>(R.id.selectSubjectBtn)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            var result = CropImage.getActivityResult(data)

            var bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, result.uri)
            imageUri.add(bitmap)
            var qImage = view?.findViewById<ImageView>(R.id.addQuestionImage)
            var aImage = view?.findViewById<ImageView>(R.id.solutionImage)
            if (resultCode == Activity.RESULT_OK){
                if (imageUri?.size!! == 1){
                    qImage?.setImageBitmap(imageUri?.get(0))
                    qImage?.isEnabled = false
                    aImage?.isEnabled = true
                }
                if (imageUri.size >= 2){
                    val recyclerView = view?.findViewById<RecyclerView>(R.id.addQuestionRecyclerView)
                    recyclerView?.layoutManager = GridLayoutManager(view?.context, 8) as RecyclerView.LayoutManager?

                    val dataList = ArrayList<BitmapImage>()
                    for (i in 0..(imageUri.size-1)){
                        dataList.add(
                            BitmapImage(
                                imageUri.get(i)
                            )
                        )

                    }

                    var line = view?.findViewById<View>(R.id.view2)
                    var line2 = view?.findViewById<View>(R.id.view3)
                    line?.isVisible = true
                    line2?.isVisible = true
//        pass the values to RvAdapter
                    val rvAdapter =
                        BitmapImageAdapter(dataList, this)
//        set the recyclerView to the adapter
                    recyclerView?.adapter = rvAdapter

                }

            }

        }
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK){

                courseName = data!!.getStringExtra("courseName")
                courseId = data.getIntExtra("courseId",-1)
                selectCourse?.text = courseName
                selectSubject?.isEnabled = true



            }
        }
        if (requestCode == 3){
            if (resultCode == Activity.RESULT_OK){
                subjectName = data!!.getStringExtra("subjectName")
                selectSubject?.text = subjectName

            }
        }

    }

    override fun onItemClicked(imagesModel: BitmapImage){


    }

    fun saveImage() : Boolean{

        if (courseName.isEmpty() or subjectName.isEmpty()){
            Toast.makeText(context,"Ders ve Konu Seçiniz",Toast.LENGTH_LONG).show()
            return false
        }
        else if(imageUri.size < 1){
            Toast.makeText(context,"Sorunuzun Fotoğrafını Ekleyiniz",Toast.LENGTH_LONG).show()
            return false

        }
        if (imageUri != null) {
            var setValue = getTime()

            for (i in imageUri){
                val smallBitmap = makeSmallerBitmap(i,1080)

                val outputStream = ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
                val byteArray = outputStream.toByteArray()

                var tableName = courseName.replace("\\s".toRegex(),"") +"_" + subjectName.replace("\\s".toRegex(),"")

                try {

                    val database = activity?.openOrCreateDatabase(selectedField, Context.MODE_PRIVATE, null)
                    database?.execSQL("CREATE TABLE IF NOT EXISTS $tableName (id INTEGER PRIMARY KEY, note VARCHAR, image BLOB, setId VARCHAR)")

                    val sqlString =
                        "INSERT INTO $tableName (note, image, setId) VALUES (?, ?, ?)"
                    val statement = database?.compileStatement(sqlString)
                    statement?.bindString(1,note)
                    statement?.bindBlob(2, byteArray)
                    statement?.bindString(3,setValue)
                    statement?.execute()


                } catch (e: Exception) {
                    e.printStackTrace()

                }


                try {

                    val database = activity?.openOrCreateDatabase(selectedField, Context.MODE_PRIVATE, null)
                    database?.execSQL("CREATE TABLE IF NOT EXISTS whole (id INTEGER PRIMARY KEY, setId VARCHAR)")

                    val sqlString =
                        "INSERT INTO whole (setId) VALUES (?)"
                    val statement = database?.compileStatement(sqlString)
                    statement?.bindString(1,setValue)
                    statement?.execute()


                } catch (e: Exception) {
                    e.printStackTrace()

                }




            }


            return true

        }
        return false
    }

    fun makeSmallerBitmap(image: Bitmap, maximumSize : Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()
        if (bitmapRatio > 1) {
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        } else {
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            width = scaledWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image,width,height,true)

    }


    fun getTime(): String{
        val date  = Calendar.getInstance().time
        var formatter = SimpleDateFormat("yyyy")
        var year = formatter.format(date)
        formatter = SimpleDateFormat("MM")
        var month = formatter.format(date)
        formatter = SimpleDateFormat("dd")
        var day = formatter.format(date)
        formatter = SimpleDateFormat("hh")
        var hour = formatter.format(date)
        formatter = SimpleDateFormat("mm")
        var min = formatter.format(date)
        formatter = SimpleDateFormat("ss")
        var sec = formatter.format(date)

        val value = year+month+day+hour+min+sec

        return value



    }








}




