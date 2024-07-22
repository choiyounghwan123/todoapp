package com.example.todoapp.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class TodoNotFoundException(id:Long) : RuntimeException("Todo not found with id $id") {
}