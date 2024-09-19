package com.example.todoapp.service

import com.example.todoapp.dto.task.TaskRequestDto
import com.example.todoapp.entity.Task
import com.example.todoapp.entity.User
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {
    private val logger:Logger = LoggerFactory.getLogger(TaskService::class.java)

    fun createTask(taskRequestDto: TaskRequestDto,userId: Long):Task{
        val task:Task = Task(
            userId = userId,
            title = taskRequestDto.title,
            description = taskRequestDto.description
        )
        return taskRepository.save(task)
    }

    fun getTaskByUserId(userId:Long):List<Task>{
        val tasks:List<Task> = taskRepository.findByUserId(userId)

        if(tasks.isEmpty()){
            logger.warn("No tasks found for user ID: $userId")
        }

        return tasks
    }

}