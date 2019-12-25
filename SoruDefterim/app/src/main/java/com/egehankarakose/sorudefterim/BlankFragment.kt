package com.egehankarakose.sorudefterim

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment



class BlankFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_blank, container, false)
        var fragment = ShowCoursesFragment()


        val ft = childFragmentManager.beginTransaction()
        ft.replace(R.id.inner_fragment,fragment).commit()











        return view

    }


}
