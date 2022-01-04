package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.HeaderItemBinding
import com.example.todoapp.databinding.RowItemBinding

class ToDoListAdapter(var onItemClicked: ((item: ToDoData) -> Unit?)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 0
        const val VIEW_TYPE_ACTIVE = 1
    }

    var dataList = emptyList<ToDoData>()

    class MyViewHolderActiveOrDone(val binding: RowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolderActiveOrDone {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolderActiveOrDone(binding)
            }
        }
    }

    class MyViewHolderHeader(val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolderHeader {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolderHeader(binding)
            }
        }

        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ACTIVE) {
            return MyViewHolderActiveOrDone.from(parent)
        }
        return MyViewHolderHeader.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = dataList[position]

        if (currentItem.viewType == VIEW_TYPE_ACTIVE) {
            (holder as MyViewHolderActiveOrDone).bind(currentItem)

            holder.binding.imageViewDelete.setOnClickListener {
                onItemClicked?.invoke(currentItem)
            }
        } else {
            (holder as MyViewHolderHeader).bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun divideListsIntoGroups(list: List<ToDoData>): List<ToDoData> {
        val uncheckedItems = list.filter { it.isActive }
        val checkedItems = list.filter { !it.isActive }
        val headerItem = list.filter { it.viewType == VIEW_TYPE_HEADER }

        return headerItem + uncheckedItems + headerItem + checkedItems
    }

    fun setData(list: List<ToDoData>) {
        this.dataList = list
        notifyDataSetChanged()
    }
}
