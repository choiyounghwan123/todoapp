package com.example.todoapp.service

import com.example.todoapp.dto.RegisterUserDto
import com.example.todoapp.entity.User
import com.example.todoapp.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    fun registerUser(registerUserDto: RegisterUserDto): User {
        if (registerUserDto.password != registerUserDto.confirmPassword) {
            throw IllegalArgumentException("Passwords do not match")
        }

        if (userRepository.findByUsername(registerUserDto.username) != null) {
            throw IllegalArgumentException("Username already exists")
        }

        if (userRepository.findByEmail(registerUserDto.email) != null) {
            throw IllegalArgumentException("Email already exists")
        }

        val hashedPassword = passwordEncoder.encode(registerUserDto.password)

        val user: User = User(
            username = registerUserDto.username,
            email = registerUserDto.email,
            password = hashedPassword
        )

        return userRepository.save(user)
    }
}