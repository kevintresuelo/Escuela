package com.kevintresuelo.escuela.screens.academicterms

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kevintresuelo.escuela.utils.EntityObject
import com.kevintresuelo.escuela.database.daos.AcademicTermDao
import com.kevintresuelo.escuela.database.entities.AcademicTerm
import kotlinx.coroutines.*
import java.util.*

class AcademicTermViewModel (
    private val mAcademicTermDao: AcademicTermDao,
    private val mAcademicTermId: Long,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main +  viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private var _academicTerm = MutableLiveData<AcademicTerm?>()
    val academicTerm
        get() = _academicTerm

    init {
        initializeAcademicTerm()
    }

    private fun initializeAcademicTerm() {
        uiScope.launch {
            _academicTerm.value = getAcademicTermFromDatabase()
        }
    }

    private suspend fun getAcademicTermFromDatabase():  AcademicTerm? {
        return withContext(Dispatchers.IO) {
            mAcademicTermDao.getAcademicTermById(mAcademicTermId)
        }
    }

    private suspend fun insert(academicTerm: AcademicTerm) {
        return withContext(Dispatchers.IO) {
            Log.d("STVM","inserting")
            mAcademicTermDao.insert(academicTerm)
        }
    }

    private suspend fun update(academicTerm: AcademicTerm) {
        return withContext(Dispatchers.IO) {
            Log.d("STVM","updating")
            mAcademicTermDao.update(academicTerm)
        }
    }

    fun saveAcademicTerm(alias: String, startDate: Long?, endDate: Long?, isActive: Int) {
        uiScope.launch { 
            val mStartDate = if (startDate != null) Date(startDate) else null
            val mEndDate = if (endDate != null) Date(endDate) else null
            val mCreatedOn = academicTerm.value?.createdOn ?: Date()
            val mLastModifiedOn = Date()

            val academicTerm = AcademicTerm(mAcademicTermId, alias, mStartDate, mEndDate, isActive, mCreatedOn, mLastModifiedOn)
            if (mAcademicTermId == EntityObject.NEW) {
                insert(academicTerm)
            } else {
                update(academicTerm)
            }

        }
    }

}