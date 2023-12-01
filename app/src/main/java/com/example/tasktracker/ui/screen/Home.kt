package com.example.tasktracker.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasktracker.data.Priority
import com.example.tasktracker.data.Task
import com.example.tasktracker.ui.theme.green
import com.example.tasktracker.ui.theme.red
import com.example.tasktracker.ui.theme.strongRed
import com.example.tasktracker.ui.theme.yellow
import com.example.tasktracker.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val tasks = viewModel.tasks.value
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val onLoading = viewModel.onLoading
    var updatedTask: Task? = null
    Scaffold(
        Modifier.padding(16.dp),
        topBar = {
            Surface(shadowElevation = 5.dp) {
                CenterAlignedTopAppBar(title = { Text("Task Management") })
            } }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (showSheet) {
                ViewBottomSheet(modalSheetState = modalSheetState, viewModel, updatedTask) {
                    showSheet = false
                }
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)) {

                Button(colors = ButtonDefaults.buttonColors(strongRed), onClick = {
                    coroutineScope.launch {
                        showSheet = true
                    }
                }, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Icon(Icons.Default.Add, contentDescription = "Icon")
                    Text("Add Task")
                }
            }
            if (tasks.isNotEmpty()) {
                LazyColumn(
                    reverseLayout = false,

                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(items = tasks, key = { task -> task.taskId!! }) { task ->
                        AnimatedVisibility(
                            visible = !onLoading,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Box(Modifier.animateEnterExit(enter = scaleIn(), exit = scaleOut())) {
                                TaskCard(task, {
                                    viewModel.deleteTask(task)
                                    updatedTask = null
                                }) {
                                    showSheet = true
                                    updatedTask = task
                                }
                            }
                        }
                    }

                }
                AnimatedVisibility(visible = onLoading) {
                    CircularProgressIndicator()
                }
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Insert a New Task")
                }
            }
        }
    }
}


@Composable
fun TaskCard(task: Task, onDelete: () -> Unit, onClick: () -> Unit) {
    val cardColor = when (task.priority) {
        Priority.LOW -> green
        Priority.MEDIUM -> yellow
        Priority.HIGH -> red
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = task.name,
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = task.taskDescription,
                    color = Color.Black,
                    style = TextStyle(fontSize = 14.sp),
                    modifier = Modifier
                        .weight(1f)
                )

                when (task.priority.name) {
                    "HIGH" -> {
                        Text(
                            text = "To Do",
                            color = Color.Black,
                            style = TextStyle(fontSize = 14.sp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    "MEDIUM" -> {
                        Text(
                            text = "In Progress",
                            color = Color.Black,
                            style = TextStyle(fontSize = 14.sp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    "LOW" -> {
                        Text(
                            text = "Done",
                            color = Color.Black,
                            style = TextStyle(fontSize = 14.sp),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

       /* Text(
            text = task.priority.name,
            color = Color.Black,
            style = TextStyle(fontSize = 12.sp),
            fontWeight = FontWeight.Bold,
        )*/
      /*  Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = task.name,

                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                       text = task.taskDescription,
                       color = Color.Black
                   )


            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }*/
    }

}


@SuppressLint("SimpleDateFormat")
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy")
    return formatter.format(Date(millis))
}

