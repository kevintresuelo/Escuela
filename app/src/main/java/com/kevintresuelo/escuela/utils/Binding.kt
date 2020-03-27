package com.kevintresuelo.escuela.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.kevintresuelo.escuela.database.entities.AcademicTerm
import org.w3c.dom.Text

@BindingAdapter("listItemTitle")
fun TextView.setListItemTitle(item: AcademicTerm?) {
    item?.let {
        text = it.alias
    }
}