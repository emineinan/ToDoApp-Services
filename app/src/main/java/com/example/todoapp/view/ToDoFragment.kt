package com.example.todoapp.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.ToDoListAdapter
import com.example.todoapp.data.model.ToDoData
import com.example.todoapp.databinding.FragmentToDoBinding
import com.example.todoapp.databinding.ToDoOverlayViewBinding
import com.example.todoapp.viewmodel.ToDoViewModel

class ToDoFragment : Fragment() {
    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val adapter: ToDoListAdapter by lazy { ToDoListAdapter() }

    private val overlayToDoBinding= ToDoOverlayViewBinding.inflate(layoutInflater) // accessing textView, buttons, editTexts

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

        // call createChannel
        binding.switchActiveOrPassive.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                startToDoService()
            } else {
                stopToDoService()
            }
        }

        /*createChannel(
            getString(R.string.todo_notification_channel_id),
            getString(R.string.todo_notification_channel_name)
        )*/

        return binding.root
    }

    private fun stopToDoService() {
        TODO("Not yet implemented")
    }

    private fun startToDoService() {
        TODO("Not yet implemented")
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

    //Service
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setShowBadge(false)
            }
            val notificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}