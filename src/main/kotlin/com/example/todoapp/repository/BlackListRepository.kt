package com.example.todoapp.repository

import com.example.todoapp.entity.BlackList
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BlackListRepository : CrudRepository<BlackList,String>{
}