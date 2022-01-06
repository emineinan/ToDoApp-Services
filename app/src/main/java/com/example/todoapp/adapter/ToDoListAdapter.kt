package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.ToDoListAdapter.*
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.HeaderItemBinding
import com.example.todoapp.databinding.RowItemBinding

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class ToDoListAdapter(var onItemClicked: ((item: ToDoData) -> Unit?)? = null) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ToDoDataDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RowViewHolder -> {
                val toDoDataItem = getItem(position) as DataItem.ToDoDataItem
                holder.bind(toDoDataItem.toDoData)

                holder.binding.imageViewDelete.setOnClickListener {
                    onItemClicked?.invoke(toDoDataItem.toDoData)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> RowViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ToDoDataItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class RowViewHolder(val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RowViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemBinding.inflate(layoutInflater, parent, false)
                return RowViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder(val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderItemBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    fun addHeaderAndSubmitList(list: List<ToDoData>?) {
        val uncheckedItems = list?.filter { it.isActive }
        val checkedItems = list?.filter { !it.isActive }

        val items = when (list) {
            null -> listOf(DataItem.Header)
            else -> listOf(DataItem.Header) + uncheckedItems!!.map { DataItem.ToDoDataItem(it) } + listOf(
                DataItem.Header
            ) + checkedItems!!.map { DataItem.ToDoDataItem(it) }
        }
        submitList(items)
    }

    class ToDoDataDiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }

    sealed class DataItem {
        data class ToDoDataItem(val toDoData: ToDoData) : DataItem() {
            override val id = toDoData.id.toLong()
        }

        object Header : DataItem() {
            override val id = Long.MIN_VALUE
        }

        abstract val id: Long
    }
}