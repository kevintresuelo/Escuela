package com.kevintresuelo.escuela.screens.academicterms

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kevintresuelo.escuela.database.entities.AcademicTerm
import com.kevintresuelo.escuela.databinding.ListItemGenericBinding
import com.kevintresuelo.escuela.utils.formatDateRange
import com.kevintresuelo.escuela.utils.formatTimeAgo

class AcademicTermsListAdapter(private val clickListener: AcademicTermListener) : ListAdapter<AcademicTerm, AcademicTermsListAdapter.ViewHolder>(AcademicTermDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemGenericBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AcademicTerm, clickListener: AcademicTermListener) {
            val res = itemView.context.resources
            binding.academicTerm = item
            binding.clickListener = clickListener
            binding.ligTextViewTitle.text = item.alias
            binding.ligTextViewSubtitle.text = formatDateRange(itemView.context, item.startDate, item.endDate) ?: formatTimeAgo(itemView.context, item.createdOn, DateUtils.DAY_IN_MILLIS)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemGenericBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class AcademicTermDiffCallback : DiffUtil.ItemCallback<AcademicTerm>() {
    override fun areItemsTheSame(oldItem: AcademicTerm, newItem: AcademicTerm): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AcademicTerm, newItem: AcademicTerm): Boolean {
        return oldItem == newItem
    }
}

class AcademicTermListener(val clickListener: (academicTermId: Long) -> Unit) {
    fun onClick(academicTerm: AcademicTerm) = clickListener(academicTerm.id)
}