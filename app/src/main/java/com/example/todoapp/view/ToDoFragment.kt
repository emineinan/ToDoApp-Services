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
import com.example.todoapp.util.startToDoService
import com.example.todoapp.util.stopToDoService
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
            adapter.addHeaderAndSubmitList(data)
        })

        binding.buttonAddTask.setOnClickListener {
            addTaskToDatabase()
        }

        binding.switchActiveOrPassive.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                requireContext().startToDoService()
            } else {
                requireContext().stopToDoService()
            }
        }

        return binding.root
    }

    private fun addTaskToDatabase() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val newTask = ToDoData(0, true, title, description)
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