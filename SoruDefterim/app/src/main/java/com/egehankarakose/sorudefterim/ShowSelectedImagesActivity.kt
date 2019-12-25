package com.egehankarakose.sorudefterim

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.egehankarakose.sorudefterim.Adapters.ViewPagerAdapter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream

class ShowSelectedImagesActivity : AppCompatActivity() {
    var setId = ""
    var courseName = ""
    var subjectName = ""
    var imagesArray: ArrayList<Bitmap> = ArrayList()
    var noteShow = ""
    var selectedField = ""

    internal lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_selected_images)



        var noteText = findViewById<TextView>(R.id.showNotes)


        var bundle :Bundle?= intent.extras
        if (bundle != null){
            setId = bundle.getString("setId")
            courseName = bundle.getString("courseName")
            subjectName = bundle.getString("subjectName")
            selectedField = bundle.getString("selectedField")


        }

        var tableName = courseName.replace("\\s".toRegex(),"") +"_" + subjectName.replace("\\s".toRegex(),"")
        getFromDatabase(tableName, setId)

        noteText.text = noteShow


        viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        val adapter = ViewPagerAdapter(this,imagesArray)
        viewPager.adapter = adapter



        var deleteBtn = findViewById<Button>(R.id.deleteBtn)
        deleteBtn.setOnClickListener{
            deleteFromDatabase(tableName,setId)
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()

        }


        var addSolBtn = findViewById<TextView>(R.id.addSolBtn)
        addSolBtn.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)

        }
    }

    fun deleteFromDatabase(tableName: String,setId: String){
        try {

            val database = this.openOrCreateDatabase(selectedField, Context.MODE_PRIVATE, null)

            database.execSQL("DELETE FROM $tableName WHERE setId= ?", arrayOf(setId))


        }catch (e: Exception) {
            e.printStackTrace()
        }


    }



    fun getFromDatabase(tableName : String, setId: String){

        try {

            val database = this.openOrCreateDatabase(selectedField, Context.MODE_PRIVATE,null)

            val cursor = database?.rawQuery("SELECT * FROM ($tableName) WHERE setId= ? ", arrayOf(setId))
            val noteIx = cursor!!.getColumnIndex("note")
            val idIx = cursor?.getColumnIndex("id")
            val image = cursor!!.getColumnIndex("image")
            val setId = cursor!!.getColumnIndex("setId")

            while (cursor!!.moveToNext()) {
                val byteArray = cursor.getBlob(image)
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                imagesArray.add(bitmap)
                noteShow = cursor.getString(noteIx)

            }

            cursor.close()


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            var result = CropImage.getActivityResult(data)
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, result.uri)

            if (resultCode == Activity.RESULT_OK){

                saveImage(bitmap)

            }


        }
    }

    fun saveImage(bitmap : Bitmap){

        val smallBitmap = makeSmallerBitmap(bitmap,1080)

        val outputStream = ByteArrayOutputStream()
        smallBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        val byteArray = outputStream.toByteArray()

        var tableName = courseName.replace("\\s".toRegex(),"") +"_" + subjectName.replace("\\s".toRegex(),"")

        try {

            val database = openOrCreateDatabase(selectedField, Context.MODE_PRIVATE, null)
            database?.execSQL("CREATE TABLE IF NOT EXISTS $tableName (id INTEGER PRIMARY KEY, note VARCHAR, image BLOB, setId VARCHAR)")

            val sqlString =
                "INSERT INTO $tableName (note, image, setId) VALUES (?, ?, ?)"
            val statement = database?.compileStatement(sqlString)
            statement?.bindString(1,noteShow)
            statement?.bindBlob(2, byteArray)
            statement?.bindString(3,setId)
            statement?.execute()

        } catch (e: Exception) {
            e.printStackTrace()
        }




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

}
