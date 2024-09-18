package com.example.todoapp.dto
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class RegisterUserDto(
    @field:NotBlank(message = "Username is required")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    val username:String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invaild email format")
    val email:String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    val password:String,

    @field:NotBlank(message = "Confirm Password is required")
    val confirmPassword:String
)
