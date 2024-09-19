package com.example.todoapp.dto.task

import com.example.todoapp.entity.Task
import java.time.LocalDateTime

data class TaskResponseDto(
    val id: Long,
    val title: String,
    val description: String,
    val createAt: LocalDateTime
) {
    companion object {
        fun fromEntity(task: Task): TaskResponseDto {
            return TaskResponseDto(
                id = task.id,
                title = task.title,
                description = task.description,
                createAt = task.createdAt
            )
        }
    }
}
