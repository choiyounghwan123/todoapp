package com.example.todoapp.controller

import com.example.todoapp.TodoService.TodoService
import com.example.todoapp.dto.TodoDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/todos")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun getAllTodos(): List<TodoDto> = todoService.getAllTodos()

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id:Long): ResponseEntity<TodoDto>{
        return ResponseEntity.ok(todoService.getTodoById(id))
    }

    @PostMapping
    fun createTodo(@RequestBody todoDto: TodoDto):
            ResponseEntity<TodoDto>{
        val createdTodo = todoService.createTodo(todoDto)

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo)
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody updatedTodoDto: TodoDto):
            ResponseEntity<TodoDto>{
        val updated = todoService.updateTodo(id,updatedTodoDto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id:Long,): ResponseEntity<Void>{
        todoService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }
}