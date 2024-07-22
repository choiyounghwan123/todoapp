package com.example.todoapp.dto

data class TodoDto(
    val id:Long = 0,
    val title:String,
    val description: String,
    val completed:Boolean
)
