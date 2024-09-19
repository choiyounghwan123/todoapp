package com.example.todoapp.util.filter

import com.example.todoapp.repository.BlackListRepository
import com.example.todoapp.util.JwtUtil
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(private val jwtUtil: JwtUtil,
    private val blackListRepository: BlackListRepository)
    :OncePerRequestFilter() {

    private val logger:Logger = LoggerFactory.getLogger(JwtFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader:String? = jwtUtil.resolveToken(request)
        val accessToken = authorizationHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        try {
            if (accessToken != null && jwtUtil.accessTokenValidateToken(accessToken)){
                if(blackListRepository.existsById(accessToken)){
                    logger.info("Token is blacklisted.")
                    SecurityContextHolder.clearContext()
                }else{
                    logger.info("Valid token. Setting authentication in SecurityContextHolder.")
                    val authentication: Authentication = jwtUtil.getAuthentication(accessToken)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }else{
                logger.warn("No valid JWT token found in request.")
            }
        }catch (e: ExpiredJwtException){
            logger.warn("Access token expired: ${e.message}")
            SecurityContextHolder.clearContext()
        }catch (e: Exception){
            logger.warn("An error occurred during token processing: ${e.message}")
            SecurityContextHolder.clearContext()
        }
        filterChain.doFilter(request,response)
    }

}