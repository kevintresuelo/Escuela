package com.kevintresuelo.escuela.screens.academicterms

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kevintresuelo.escuela.database.daos.AcademicTermDao

class AcademicTermViewModelFactory (
    private val academicTermDao: AcademicTermDao,
    private val academicTermId: Long,
    private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcademicTermViewModel::class.java)) {
            return AcademicTermViewModel(
                academicTermDao,
                academicTermId,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}