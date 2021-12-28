package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.RowItemBinding
import com.example.todoapp.util.ToDoDiffUtil
import com.example.todoapp.viewmodel.ToDoViewModel

class ToDoListAdapter (private val toDoViewModel: ToDoViewModel): RecyclerView.Adapter<ToDoListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    class MyViewHolder(val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)

        holder.binding.imageViewDelete.setOnClickListener {
             toDoViewModel.deleteData(currentItem)
             setData(dataList)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newList: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, newList)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = newList
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}