package com.example.tasktracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = 0,
    val name: String,
    val priority: Priority,
    val date: String,
    val taskDescription: String

)



enum class Priority {
    LOW, MEDIUM, HIGH
}