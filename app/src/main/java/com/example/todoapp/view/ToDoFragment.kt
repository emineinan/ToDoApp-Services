package com.example.todoapp.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startForegroundService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.adapter.ToDoListAdapter
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.service.INTENT_COMMAND
import com.example.todoapp.service.ToDoService
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
                startToDoService()
            } else {
                stopToDoService()
            }
        }

        return binding.root
    }

    private fun startToDoService(command: String = "") {
        /*  ToDoService.startService(requireContext())
          Toast.makeText(requireContext(), "Service started.", Toast.LENGTH_SHORT).show()*/
        val intent = Intent(requireContext(), ToDoService::class.java)
        if (command.isNotBlank()) intent.putExtra(INTENT_COMMAND, command)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(intent)
            Toast.makeText(requireContext(), "Service başalatıldı", Toast.LENGTH_SHORT).show()
        } else {
            requireContext().startService(intent)
            Toast.makeText(requireContext(), "Service başaltıldı", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopToDoService() {
        val intent = Intent(requireContext(), ToDoService::class.java)
        requireContext().stopService(intent)
        Toast.makeText(requireContext(), "Service durduruldu", Toast.LENGTH_SHORT).show()
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