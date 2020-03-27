package com.kevintresuelo.escuela.screens.dashboard


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.kevintresuelo.escuela.R
import com.kevintresuelo.escuela.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        binding.button.setOnClickListener(
            Navigation.createNavigateOnClickListener(DashboardFragmentDirections.actionDashboardFragmentToAcademicTermsFragment())
        )

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.editor, menu)
    }


}
