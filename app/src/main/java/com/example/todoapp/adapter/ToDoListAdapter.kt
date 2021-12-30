package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.RowItemActiveBinding
import com.example.todoapp.databinding.RowItemDoneBinding
import com.example.todoapp.util.ToDoDiffUtil

class ToDoListAdapter(var onItemClicked: ((item: ToDoData) -> Unit?)? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ACTIVE = 1
        const val VIEW_TYPE_DONE = 2
    }

    var dataList = emptyList<ToDoData>()

    class MyViewHolderActive(val binding: RowItemActiveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolderActive {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemActiveBinding.inflate(layoutInflater, parent, false)
                return MyViewHolderActive(binding)
            }
        }
    }

    class MyViewHolderDone(val binding: RowItemDoneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolderDone {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemDoneBinding.inflate(layoutInflater, parent, false)
                return MyViewHolderDone(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ACTIVE) {
            return MyViewHolderActive.from(parent)
        }
        return MyViewHolderDone.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = dataList[position]

        if (currentItem.viewType == VIEW_TYPE_ACTIVE) {
            (holder as MyViewHolderActive).bind(currentItem)

            holder.binding.imageViewDeleteActive.setOnClickListener {
                onItemClicked?.invoke(currentItem)
            }
        } else {
            (holder as MyViewHolderDone).bind(currentItem)

            holder.binding.imageViewDeleteDone.setOnClickListener {
                onItemClicked?.invoke(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newList: List<ToDoData>) {
        this.dataList = newList
        notifyDataSetChanged()
    }
}
