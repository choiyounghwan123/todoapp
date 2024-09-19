package com.example.todoapp.controller

import com.example.todoapp.dto.register.RegisterUserDto
import com.example.todoapp.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid registerUserDto: RegisterUserDto)
    :ResponseEntity<String>{
        return try{
            userService.registerUser(registerUserDto)
            ResponseEntity.ok("User registered successfully")
        }catch (e: IllegalArgumentException){
            ResponseEntity.badRequest().body(e.message)
        }
    }
}