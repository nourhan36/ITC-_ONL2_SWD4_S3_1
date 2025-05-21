package com.example.itc__onl2_swd4_s3_1.features.home.presentation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.itc__onl2_swd4_s3_1.data.entity.HabitEntity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitListItem(
    habit: HabitEntity,
    onEdit: (HabitEntity) -> Unit,
    onDeleteConfirmed: (HabitEntity) -> Unit,
    onToggleComplete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var resetSwipe by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState()
    val direction = dismissState.dismissDirection


    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            DismissValue.DismissedToStart -> {
                if (!showDialog) {
                    showDialog = true
                }
            }
            DismissValue.DismissedToEnd -> {
                onEdit(habit)
                resetSwipe = true
            }
            else -> Unit
        }
    }

    // Reset swipe if requested
    LaunchedEffect(resetSwipe) {
        if (resetSwipe) {
            dismissState.reset()
            resetSwipe = false
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                resetSwipe = true
            },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete this habit?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirmed(habit)
                    showDialog = false
                    resetSwipe = true
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    resetSwipe = true
                }) {
                    Text("Cancel", color = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            if (direction != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight()
                            .align(
                                if (direction == DismissDirection.StartToEnd)
                                    Alignment.CenterStart
                                else
                                    Alignment.CenterEnd
                            )
                            .background(
                                color = if (direction == DismissDirection.StartToEnd)
                                    Color(0xFF4CAF50) else Color.Red,
                                        shape = RoundedCornerShape(8.dp) // flat background

                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (direction == DismissDirection.StartToEnd)
                                Icons.Default.Edit else Icons.Default.Delete,
                            contentDescription = if (direction == DismissDirection.StartToEnd)
                                "Edit" else "Delete",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        dismissContent = {
            HabitCard(
                habit = habit,
                onCheck = onToggleComplete
            )
        }
    )
}

