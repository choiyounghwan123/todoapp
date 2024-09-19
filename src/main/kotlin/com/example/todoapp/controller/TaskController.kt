package com.example.todoapp.controller

import com.example.todoapp.dto.task.TaskRequestDto
import com.example.todoapp.dto.task.TaskResponseDto
import com.example.todoapp.entity.Task
import com.example.todoapp.service.TaskService
import com.example.todoapp.util.CustomUserDetails
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {
    @GetMapping
    fun getTaskByUser(@AuthenticationPrincipal userDetails: CustomUserDetails):ResponseEntity<List<Task>>{
        val task:List<Task> = taskService.getTaskByUserId(userDetails.getUserId())
        return ResponseEntity.ok(task)
    }

    @PostMapping
    fun createTask( @Valid @RequestBody taskRequestDto: TaskRequestDto,
                   @AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<TaskResponseDto>{
        val createTask = taskService.createTask(taskRequestDto,userDetails.getUserId())
        val response = TaskResponseDto.fromEntity(createTask)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}