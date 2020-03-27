package com.kevintresuelo.escuela.screens.academicterms


import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.kevintresuelo.escuela.R
import com.kevintresuelo.escuela.database.EscuelaDatabase
import com.kevintresuelo.escuela.database.daos.AcademicTermDao
import com.kevintresuelo.escuela.databinding.FragmentGenericListBinding
import com.kevintresuelo.escuela.utils.EntityObject
import com.kevintresuelo.escuela.utils.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class AcademicTermsListFragment : Fragment() {

    private lateinit var binding: FragmentGenericListBinding
    private lateinit var application: Application
    private lateinit var academicTermDao: AcademicTermDao
    private lateinit var academicTermsListViewModelFactory: AcademicTermsListViewModelFactory
    private lateinit var academicTermsListViewModel: AcademicTermsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /**
         * Initializes data binding with the layout and view container
         */
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_generic_list, container, false)

        /**
         * Hides the keyboard in the activity if it is shown. This is a temporary
         * hack solution to prevent the keyboard from being persistently shown
         * after going back to the add/edit fragment of this section.
         */
        hideKeyboard(activity as Activity)

        /**
         * Initializes some variables needed for the ViewModel
         */
        application = requireNotNull(this.activity).application
        academicTermDao = EscuelaDatabase.getInstance(application).academicTermDao
        academicTermsListViewModelFactory = AcademicTermsListViewModelFactory(academicTermDao, application)
        academicTermsListViewModel = ViewModelProvider(this, academicTermsListViewModelFactory).get(AcademicTermsListViewModel::class.java)

        /**
         * Initializes the adapter and the callback method once a list item has
         * been clicked.
         */
        val academicTermsListAdapter = AcademicTermsListAdapter(AcademicTermListener {
            academicTermsListViewModel.onAcademicTermClicked(it)
        })
        binding.fglRecyclerViewList.adapter = academicTermsListAdapter

        /**
         * Observes the LiveData list for AcademicTerm in the ViewModel, and then
         * sends the result to the adapter for processing into the RecyclerView.
         *
         * Shows an empty view if the list is deemed empty, otherwise the list
         * is shown.
         */
        academicTermsListViewModel.academicTerms.observe(viewLifecycleOwner, Observer { academicTermsList ->
            academicTermsList?.let {
                academicTermsListAdapter.submitList(it)
                if (it.isEmpty()) {
                    binding.fglRecyclerViewList.visibility = View.GONE
                    binding.fglLinearLayoutEmptyViewList.visibility = View.VISIBLE
                } else {
                    binding.fglRecyclerViewList.visibility = View.VISIBLE
                    binding.fglLinearLayoutEmptyViewList.visibility = View.GONE
                }
            }
        })

        /**
         * Initializes the empty view properties for the AcademicTerm
         */
        binding.fglImageViewEmptyViewIcon.setImageResource(R.drawable.ic_calendar_question)
        binding.fglTextViewEmptyViewTitle.setText(R.string.academicterm_list_empty_view_title)
        binding.fglTextViewEmptyViewSubtitle.setText(R.string.academicterm_list_empty_view_message)

        /**
         * Observes for changes in the ViewModel to trigger navigation to a
         * specific AcademicTerm add/edit fragment; navigates to it when
         * necessary, and cleans up the ViewModel afterwards.
         */
        academicTermsListViewModel.navigateToAcademicTerm.observe(viewLifecycleOwner, Observer { academicTermId ->
            academicTermId?.let {
                this.findNavController().navigate(AcademicTermsListFragmentDirections.actionAcademicTermsListFragmentToEditAcademicTermFragment(it))
                academicTermsListViewModel.doneNavigating()
            }
        })

        /**
         * Creates a navigation to the add/edit fragment with the necessary
         * ID for the creation of a new Entity Object for AcademicTerm.
         */
        binding.fglFloatingActionButtonAdd.setOnClickListener(
            Navigation.createNavigateOnClickListener(AcademicTermsListFragmentDirections.actionAcademicTermsListFragmentToEditAcademicTermFragment(EntityObject.NEW))
        )

        /**
         * Hides the FAB when scrolling downwards to prevent blocking the list
         * items in the UI.
         */
        binding.fglRecyclerViewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (binding.fglFloatingActionButtonAdd.isOrWillBeShown) {
                    if (dy > 0) {
                        binding.fglFloatingActionButtonAdd.hide()
                    } else {
                        binding.fglFloatingActionButtonAdd.show()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        /**
         * Returns the root view of the binding as the view of the fragment
         */
        return binding.root
    }

}
