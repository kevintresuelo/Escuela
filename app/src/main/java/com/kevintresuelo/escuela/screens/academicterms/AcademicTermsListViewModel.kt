package com.kevintresuelo.escuela.screens.academicterms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kevintresuelo.escuela.database.daos.AcademicTermDao
import kotlinx.coroutines.*

class AcademicTermsListViewModel (
    private val academicTermDao: AcademicTermDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    var academicTerms = academicTermDao.getAcademicTerms()

    private val _navigateToAcademicTerm = MutableLiveData<Long>()
    val navigateToAcademicTerm
        get() = _navigateToAcademicTerm

    fun onAcademicTermClicked(academicTermId: Long) {
        _navigateToAcademicTerm.value = academicTermId
    }

    fun doneNavigating() {
        _navigateToAcademicTerm.value = null
    }

}