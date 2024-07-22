package com.example.todoapp.repository

import com.example.todoapp.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoRepository :JpaRepository<Todo,Long>{
}