package com.kevintresuelo.escuela.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "academic_terms")
data class AcademicTerm (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Long,

    // Alias of this term
    // e.g. 1st Semester, 2nd Semester, 3rd Trimester
    val alias: String,

    @ColumnInfo(name = "start_date")
    val startDate: Date?,

    @ColumnInfo(name = "end_date")
    val endDate: Date?,

    @ColumnInfo(name = "is_active")
    val isActive: Int,

    @ColumnInfo(name = "date_created")
    val createdOn: Date,

    @ColumnInfo(name = "date_modified")
    val lastModifiedOn: Date?

)