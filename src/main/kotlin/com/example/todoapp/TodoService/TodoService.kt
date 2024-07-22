package com.example.todoapp.TodoService

import com.example.todoapp.dto.TodoDto
import com.example.todoapp.entity.Todo
import com.example.todoapp.exception.TodoNotFoundException
import com.example.todoapp.repository.TodoRepository
import org.springframework.stereotype.Service

@Service
class TodoService(private val todoRepository: TodoRepository) {

    fun getAllTodos(): List<TodoDto> = todoRepository.findAll().map { it.toDto() }

    fun getTodoById(id:Long): TodoDto =
        todoRepository.findById(id).orElseThrow { TodoNotFoundException(id) }.toDto()

    fun createTodo(todoDto: TodoDto): TodoDto{
        val todo = Todo(
            title = todoDto.title,
            description = todoDto.description,
            completed = todoDto.completed
        )

        return todoRepository.save(todo).toDto()
    }

    fun updateTodo(id:Long, updatedTodoDto: TodoDto): TodoDto{
        val todo = todoRepository.findById(id).orElseThrow{
            TodoNotFoundException(id)
        }
        todo.title = updatedTodoDto.title
        todo.description = updatedTodoDto.description
        todo.completed = updatedTodoDto.completed
        return todoRepository.save(todo).toDto()
    }

    fun deleteTodo(id:Long) = todoRepository.deleteById(id)

    private fun Todo.toDto() = TodoDto(
        id = this.id,
        title = this.title,
        description = this.description,
        completed = this.completed
    )
}