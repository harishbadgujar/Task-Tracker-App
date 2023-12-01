package com.example.tasktracker.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasktracker.data.Priority
import com.example.tasktracker.data.Task
import com.example.tasktracker.ui.theme.strongRed
import com.example.tasktracker.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewBottomSheet(
    modalSheetState: SheetState,
    viewModel: HomeViewModel,
    task: Task? = null,
    onDismissRequest: () -> Unit,
) {
    var taskName by remember {
        mutableStateOf(
            if (task?.name == null) {
                ""
            } else {
                task.name
            }
        )
    }

    var taskDescription by remember {
        mutableStateOf(
            if (task?.taskDescription == null) {
                ""
            } else {
                task.taskDescription
            }
        )
    }

    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    val taskDate by remember {
        mutableStateOf((datePickerState.selectedDateMillis?.let {
            if (task?.date == null) {
                convertMillisToDate(
                    it
                )
            } else {
                task.date
            }

        }.toString()))
    }
    val coroutineScope = rememberCoroutineScope()
    val options = listOf("To Do" ,  "In Progress" , "Done")

    var chosenDropdown by remember {
        mutableStateOf(
            if (task?.name == null) {
                "To Do"
            } else {
                when(task.priority){
                    Priority.HIGH -> "To Do"
                    Priority.MEDIUM -> "In Progress"
                    Priority.LOW -> "Done"
                }
            }
        )
    }
    val selectedOptionText = when (chosenDropdown) {
        "To Do" -> Priority.HIGH
        "In Progress" -> Priority.MEDIUM
        "Done" -> Priority.LOW
        else -> {
            Priority.LOW
        }
    }
    var dropDownExposed by remember { mutableStateOf(false) }
    ModalBottomSheet(

        modifier = Modifier.height((LocalConfiguration.current.screenHeightDp / 1.5).dp),
        onDismissRequest = onDismissRequest,
        sheetState = modalSheetState,
        dragHandle = { BottomSheetDefaults.ExpandedShape }) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxHeight(),

            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                if (task?.name == null) {
                    "Add a New Task"
                } else {
                    "Update Task"
                },
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )


            Column {
                Text("Task Name")
                OutlinedTextField(

                    value = taskName,
                    onValueChange = { taskName = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Column {
                Text("Task Description")
                OutlinedTextField(

                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }


            ExposedDropdownMenuBox(
                expanded = dropDownExposed,
                onExpandedChange = {
                    dropDownExposed = !dropDownExposed
                }
            ) {
                OutlinedTextField(
                    value = chosenDropdown,
                    readOnly = true,
                    onValueChange = { },
                    label = { Text("Task Status") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dropDownExposed
                        )
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = dropDownExposed,
                    onDismissRequest = {
                        dropDownExposed = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption) },
                            onClick = {
                                chosenDropdown = selectionOption
                                dropDownExposed = false
                            }
                        )
                    }
                }
            }

            Button(colors = ButtonDefaults.buttonColors(strongRed), onClick = {

                onDismissRequest()
                coroutineScope.launch {

                    viewModel.insertTask(
                        Task(
                            taskId = if (task?.taskId != null) task.taskId else null ,
                            name = taskName,
                            date = taskDate,
                            priority = selectedOptionText,
                            taskDescription = taskDescription
                        )
                    )

                }

            }, modifier = Modifier.fillMaxWidth()) {
                Text(
                    if (task?.name == null) {
                        "Insert Task"
                    } else {
                        "Update Task"
                    }
                )
            }

        }

    }

}