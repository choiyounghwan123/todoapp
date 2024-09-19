package com.example.todoapp.service

import com.example.todoapp.entity.BlackList
import com.example.todoapp.exception.TokenNotFoundException
import com.example.todoapp.repository.BlackListRepository
import com.example.todoapp.repository.RefreshTokenRepository
import com.example.todoapp.util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class LogoutService(
    private val jwtUtil: JwtUtil,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val blackListRepository: BlackListRepository
) : LogoutHandler {
    private val logger: Logger = LoggerFactory.getLogger(LogoutService::class.java)

    @Transactional
    override fun logout(request: HttpServletRequest, response: HttpServletResponse?, authentication: Authentication?) {
        val authorizationHeader = jwtUtil.resolveToken(request)
        val accessToken: String? = authorizationHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (accessToken == null) {
            logger.error("Token not found")
            throw TokenNotFoundException("Token not found")
        }

        if (!jwtUtil.accessTokenValidateToken(accessToken)) {
            logger.error("Token not valid $accessToken")
            response?.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }

        val email:String = jwtUtil.getEmail(accessToken)
        refreshTokenRepository.deleteById(email)

        val expirationMillis = java.time.Duration.between(Instant.now(), jwtUtil.getExpirationDate(accessToken).toInstant()).toMillis()
        val blackList:BlackList = BlackList(accessToken,expirationMillis)
        blackListRepository.save(blackList)

        logger.info("User successfully logged out $email")
        response?.status = HttpServletResponse.SC_OK
    }
}