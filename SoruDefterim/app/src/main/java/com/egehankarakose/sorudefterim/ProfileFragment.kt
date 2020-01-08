package com.egehankarakose.sorudefterim

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.egehankarakose.sorudefterim.Models.ImagesModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    var setIds : ArrayList<String> = ArrayList()
    var setIdsForCheck : ArrayList<String> = ArrayList()

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
      var view = inflater.inflate(R.layout.fragment_profile, container, false)

      var tytQueNo = view.findViewById<TextView>(R.id.questionNo1)
      var tytAnsNo = view.findViewById<TextView>(R.id.answerNo1)

      var aytQueNo = view.findViewById<TextView>(R.id.questionNo2)
      var aytAnsNo = view.findViewById<TextView>(R.id.answerNo2)



      this.getFromDatabase("TYT")
      tytQueNo.text = setIds.size.toString()
      tytAnsNo.text = setIdsForCheck.size.toString()

      setIds.clear()
      setIdsForCheck.clear()

      this.getFromDatabase("AYT")
      aytQueNo.text = setIds.size.toString()
      aytAnsNo.text = setIdsForCheck.size.toString()

      setIds.clear()
      setIdsForCheck.clear()


      var signOutBtn = view.findViewById<Button>(R.id.signOutButton)
      signOutBtn.setOnClickListener {
          signOut()


      }


      return view


  }
    private fun signOut(){

        FirebaseAuth.getInstance().signOut()
        var intent = Intent(view?.context,LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    fun getFromDatabase(selectedField: String){

        try {

            val database = activity?.openOrCreateDatabase(selectedField, Context.MODE_PRIVATE,null)

            val cursor = database?.rawQuery("SELECT * FROM whole",null)
            val idIx = cursor?.getColumnIndex("id")
            val setId = cursor!!.getColumnIndex("setId")

            while (cursor!!.moveToNext()) {

                if (cursor.getString(setId) !in setIds ){
                    setIds.add(cursor.getString(setId))
                }else{
                    if (cursor.getString(setId) !in setIdsForCheck){
                        setIdsForCheck.add(cursor.getString(setId))
                    }
                }

            }

            cursor.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}