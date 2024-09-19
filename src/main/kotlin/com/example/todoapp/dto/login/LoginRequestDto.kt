package com.example.todoapp.dto.login

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequestDto (
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email:String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be least 6 characters long")
    val password:String
)