package com.example.todoapp.dto.task

import jakarta.validation.constraints.NotBlank

data class TaskRequestDto(
    @field:NotBlank(message = "Title is required")
    val title:String,

    @field:NotBlank(message = "description is required")
    val description:String
)
