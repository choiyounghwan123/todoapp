package com.example.todoapp.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint : AuthenticationEntryPoint{
    private val logger:Logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        logger.error("Unauthorized request to ${request?.requestURI}: ${authException?.message}")

        response?.contentType = "application/json"
        response?.status = HttpServletResponse.SC_UNAUTHORIZED

        response?.writer?.write("{\"error\": \"Unauthorized\", \"message\": \"Access denied\"}")

    }
}