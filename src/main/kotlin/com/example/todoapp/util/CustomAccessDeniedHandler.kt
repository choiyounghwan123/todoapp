package com.example.todoapp.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler{

    private val logger:Logger = LoggerFactory.getLogger(CustomAccessDeniedHandler::class.java)

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        logger.error("Access denied to ${request?.requestURI}: ${accessDeniedException?.message}")

        response?.contentType = "application/json"
        response?.status = HttpServletResponse.SC_FORBIDDEN

        response?.writer?.write("{\"error\": \"Forbidden\", \"message\": \"Access denied\"}")
    }
}