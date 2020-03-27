package com.kevintresuelo.escuela.screens.academicterms


import android.app.Application
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.util.Pair
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kevintresuelo.escuela.R
import com.kevintresuelo.escuela.database.EscuelaDatabase
import com.kevintresuelo.escuela.database.daos.AcademicTermDao
import com.kevintresuelo.escuela.databinding.FragmentAcademicTermBinding
import com.kevintresuelo.escuela.utils.EntityObject
import com.kevintresuelo.escuela.utils.formatDateRange
import com.kevintresuelo.escuela.utils.isBlankOrEmpty

class AcademicTermFragment : Fragment() {

    private var academicTermId = EntityObject.NEW

    private lateinit var binding: FragmentAcademicTermBinding
    private lateinit var application: Application
    private lateinit var academicTermDao: AcademicTermDao
    private lateinit var academicTermViewModelFactory: AcademicTermViewModelFactory
    private lateinit var academicTermViewModel: AcademicTermViewModel

    private lateinit var builder: MaterialDatePicker.Builder<Pair<Long, Long>>
    private lateinit var picker: MaterialDatePicker<*>
    private val today = MaterialDatePicker.todayInUtcMilliseconds()
    private var savedPair: Pair<Long, Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /**
         * Initializes data binding with the layout and view container
         */
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_academic_term, container, false)

        /**
         * Tells the parent activity that this fragment has options menu enabled
         */
        setHasOptionsMenu(true)

        /**
         * Initializes some variables needed for the ViewModel
         */
        application = requireNotNull(this.activity).application
        academicTermDao = EscuelaDatabase.getInstance(application).academicTermDao
        val arguments = AcademicTermFragmentArgs.fromBundle(arguments!!)
        academicTermId = arguments.academicTermId
        academicTermViewModelFactory = AcademicTermViewModelFactory(academicTermDao, academicTermId, application)
        academicTermViewModel = ViewModelProvider(this, academicTermViewModelFactory).get(AcademicTermViewModel::class.java)

        /**
         * Binds the ViewModel to the data class in the layout
         */
        binding.academicTermViewModel = academicTermViewModel

        /**
         * Sets the lifecycle owner of the Data Binding to this fragment, so that
         * the ViewModels created by this fragment behave appriopriately with
         * respect to this fragment's lifecycle
         */
        binding.lifecycleOwner = this

        /**
         * Initializes the savedPair to null as a placeholder if ever such data
         * is unavailable.
         */
        savedPair = Pair(null, null)

        /**
         * Observes the LiveData AcademicTerm with the specified ID, parses and
         * displays the result if one has been found
         */
        academicTermViewModel.academicTerm.observe(viewLifecycleOwner, Observer {
            it?.let { academicTerm ->
                binding.festTextInputEditTextAcademicTermAlias.setText(academicTerm.alias)
                binding.festTextInputEditTextAcademicTermCoveredDate.setText(formatDateRange(binding.festTextInputEditTextAcademicTermCoveredDate.context, academicTerm.startDate, academicTerm.endDate))
                if (academicTerm.startDate != null && academicTerm.endDate != null) {
                    savedPair = Pair(academicTerm.startDate.time, academicTerm.endDate.time)
                }
                if (academicTerm.isActive == 1) {
                    binding.festCheckBoxAcademicTermIsActive.isChecked = true
                }
            }
        })

        /**
         * Initializes the MaterialDatePicker.Builder for the covered date range
         */
        builder = MaterialDatePicker.Builder.dateRangePicker()

        /**
         * Restricts input on the AcademicTerm's covered date range
         * TextInputEditText by disabling focusability and setting the input type
         * to null, so that the input will only be through the MaterialDatePicker
         */
        binding.festTextInputEditTextAcademicTermCoveredDate.inputType = InputType.TYPE_NULL
        binding.festTextInputEditTextAcademicTermCoveredDate.isFocusable = false

        /**
         * Shows the date range picker for the covered date range of AcademicTerm
         *
         * The selection is set here so that each time that the savedPair is
         * changed, the selection on the date range picker will also reflect the
         * changes.
         */
        binding.festTextInputEditTextAcademicTermCoveredDate.setOnClickListener {
            builder.setSelection(savedPair)
            picker = builder.build()

            /**
             * Saves the selection to a temmporary variable so that the data can be
             * utilized to be displayed on the UI
             */
            picker.addOnPositiveButtonClickListener {
                savedPair = picker.selection as Pair<Long, Long>
                val tempSavedPair = savedPair!!
                binding.festTextInputEditTextAcademicTermCoveredDate.setText(formatDateRange(binding.festTextInputEditTextAcademicTermCoveredDate.context, tempSavedPair.first, tempSavedPair.second))
            }
            picker.addOnCancelListener {
                savedPair = null
            }
            picker.addOnNegativeButtonClickListener {
                savedPair = null
            }

            if (!picker.isVisible) {
                picker.show(parentFragmentManager, picker.toString())
            }
        }

        /**
         * Returns the root view of the binding as the view of the fragment
         */
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.editor, menu)

        /**
         * Hides the delete menu action if the AcademicTerm is new
         */
        if (academicTermId == EntityObject.NEW) {
            menu.findItem(R.id.menu_delete).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /**
         * Identifies and acts upon the selected options menu item
         */
        when (item.itemId) {
            R.id.menu_cancel -> Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
            R.id.menu_delete -> Toast.makeText(activity, "Delete", Toast.LENGTH_SHORT).show()
            R.id.menu_save -> save()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        var newPair = savedPair

        if (newPair == null) {
            newPair = Pair(null, null)
        }

        val alias = binding.festTextInputEditTextAcademicTermAlias.text.toString()
        val startDate: Long? = newPair.first
        val endDate: Long? = newPair.second
        val isActive = if (binding.festCheckBoxAcademicTermIsActive.isChecked) 1 else 0

        if (alias.isBlankOrEmpty()) {
            showError(R.string.error_dialog_message_academicterm_alias_empty)
            return
        }

        academicTermViewModel.saveAcademicTerm(alias, startDate, endDate, isActive)
        // TODO: implement a feedback mechanism where it checks if the entity has been saved before leaving the fragment. For now, just leave the fragment without checking
        binding.root.requestFocus()
        findNavController().navigateUp()

    }

    private fun showError(errorStringResId: Int) {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.error_dialog_title)
            .setMessage(errorStringResId)
            .setPositiveButton(R.string.error_dialog_action_ok, null)
            .show()
    }


}
