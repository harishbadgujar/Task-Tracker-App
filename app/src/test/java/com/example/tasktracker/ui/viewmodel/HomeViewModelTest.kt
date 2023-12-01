package com.example.tasktracker.ui.viewmodel

import com.example.tasktracker.data.Priority
import com.example.tasktracker.data.Task
import com.example.tasktracker.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModelTest {


    @Mock
    private lateinit var taskRepository: TaskRepository

    private lateinit var taskViewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockitoAnnotations.initMocks(this)
        taskViewModel = HomeViewModel(taskRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test insertTask`() = runTest {
        // Given
        val task = Task(1, "Task", priority = Priority.HIGH, "30-11-23","Done")
        val taskList = listOf(task)

        `when`(taskRepository.getAllTask()).thenReturn(taskList)

        // When
        taskViewModel.insertTask(task)

        // Then
        assertEquals(true, taskViewModel.onLoading)

        advanceUntilIdle()

        assertEquals(false, taskViewModel.onLoading)
        assertEquals(taskList, taskViewModel.tasks.value)
        Mockito.verify(taskRepository).insertTask(task)
    }


    @Test
    fun `insert task into repository`() = runTest {
        // Given
        val task = Task(1, "Task Title", priority = Priority.HIGH, "30-11-23","Done")
        // When
        taskViewModel.insertTask(task)
        // Then
        verify(taskRepository).insertTask(task)
    }
}