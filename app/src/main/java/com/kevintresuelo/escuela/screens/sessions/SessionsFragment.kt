package com.kevintresuelo.escuela.screens.sessions


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.kevintresuelo.escuela.R

/**
 * A simple [Fragment] subclass.
 */
class SessionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sessions, container, false)
    }


}
