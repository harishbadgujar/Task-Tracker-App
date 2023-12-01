package com.example.tasktracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasktracker.data.TaskRepository

class ViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return modelClass.getDeclaredConstructor(TaskRepository::class.java)
                .newInstance(repository)
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}