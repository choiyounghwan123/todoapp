package com.example.todoapp.service

import com.example.todoapp.entity.User
import com.example.todoapp.repository.UserRepository
import com.example.todoapp.util.CustomUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService (private val userRepository: UserRepository)
    :UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found with email: $username")
        return CustomUserDetails(user)
    }
}