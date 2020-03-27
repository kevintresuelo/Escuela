package com.kevintresuelo.escuela.screens.subjects


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kevintresuelo.escuela.R

/**
 * A simple [Fragment] subclass.
 */
class SubjectsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subjects, container, false)
    }


}
