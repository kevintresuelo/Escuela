package com.kevintresuelo.escuela.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kevintresuelo.escuela.database.entities.AcademicTerm

@Dao
interface AcademicTermDao {

    @Insert
    fun insert(academicTerm: AcademicTerm)

    @Update
    fun update(vararg academicTerms: AcademicTerm)

    @Delete
    fun delete(vararg academicTerms: AcademicTerm)

    @Query("SELECT * FROM academic_terms")
    fun getAcademicTerms(): LiveData<List<AcademicTerm>?>

    @Query("SELECT * FROM academic_terms WHERE _id = :academicTermId")
    fun getAcademicTermById(academicTermId: Long): AcademicTerm?

}