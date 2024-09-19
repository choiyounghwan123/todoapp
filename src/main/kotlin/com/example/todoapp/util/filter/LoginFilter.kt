package com.example.todoapp.util.filter

import com.example.todoapp.dto.login.LoginRequestDto
import com.example.todoapp.util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException

class LoginFilter(authenticationManager:AuthenticationManager,
    private val objectMapper: ObjectMapper,
    private val jwtUtil: JwtUtil) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/api/auth/login","POST"),
    authenticationManager
){
        companion object{
            private val logger:Logger = LoggerFactory.getLogger(LoginFilter::class.java)
        }

    override fun attemptAuthentication(request: HttpServletRequest,
                                       response: HttpServletResponse)
    : Authentication {
        val loginRequest = try {
            objectMapper.readValue(request.inputStream, LoginRequestDto::class.java)
        }catch (e: IOException){
            throw RuntimeException("Failed to parse authentication request body ${e.message}")
        }

        logger.info(loginRequest)
        val authRequest = UsernamePasswordAuthenticationToken(loginRequest.email,loginRequest.password)
        return authenticationManager.authenticate(authRequest)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        logger.info("Login successful")

        val accessToken:String = jwtUtil.generateAccessToken(authResult)
        val refreshToken:String = jwtUtil.generateRefreshToken(authResult)

        response?.setHeader("Authorization", "Bearer $accessToken")
        response?.addCookie(createCookie("refresh",refreshToken))
        response?.status = HttpStatus.OK.value() // HTTP STATUS CODE 200
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        failed: AuthenticationException?
    ) {
        logger.error("Login failed")
        response?.status = HttpStatus.UNAUTHORIZED.value() // HTTP STATUS CODE 401
    }

    private fun createCookie(name: String, value:String):Cookie{
        return Cookie(name,value).apply {
            maxAge = 7 * 24 * 60 * 60
            isHttpOnly = true
            path = "/"
        }
    }
}