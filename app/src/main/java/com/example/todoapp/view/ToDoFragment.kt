package com.example.todoapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.ToDoListAdapter
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.viewmodel.ToDoViewModel

class ToDoFragment : Fragment() {
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val adapter: ToDoListAdapter by lazy { ToDoListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)

        setAdapter()


        toDoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            val uncheckedItems = data.filter { it.isActive }
            val checkedItems = data.filter { !it.isActive }

            if (uncheckedItems.isEmpty()) {
                val headerActive = ToDoData(0, 0, "ACTIVE", true, "", "")
                toDoViewModel.insertData(headerActive)
            }/* else if (uncheckedItems.isNotEmpty() && checkedItems.isEmpty()) {
                val headerActive = ToDoData(0, 0, "DONE", true, "", "")
                toDoViewModel.insertData(headerActive)
                adapter.setData(data)
            } else {
                adapter.setData(data)
            }*/
            adapter.setData(data)

        })

        binding.buttonAddTask.setOnClickListener {
            addTaskToDatabase()
        }

        return binding.root
    }

    private fun addTaskToDatabase() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val newTask = ToDoData(0, 1, "", true, title, description)
        toDoViewModel.insertData(newTask)

        clearInputFields()
    }

    private fun clearInputFields() {
        binding.editTextTitle.text.clear()
        binding.editTextDescription.text.clear()
    }

    private fun setAdapter() {
        val recyclerView = binding.recyclerViewTasks
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.apply {
            onItemClicked = { currentItem ->
                toDoViewModel.deleteOrUpdateData(currentItem)
            }
        }
    }

}