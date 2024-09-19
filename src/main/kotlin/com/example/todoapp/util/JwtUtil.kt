package com.example.todoapp.util

import com.example.todoapp.entity.RefreshToken
import com.example.todoapp.entity.User
import com.example.todoapp.exception.TokenNotFoundException
import com.example.todoapp.repository.RefreshTokenRepository
import com.example.todoapp.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtUtil(
    @Value("\${spring.data.jwt.access_secret}") val accessSecretKey: String,
    @Value("\${spring.data.jwt.refresh_secret}") val refreshSecretKey: String,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository
){
    companion object{
        private val logger:Logger = LoggerFactory.getLogger(JwtUtil::class.java)
    }

    private val accessKey:Key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey)) }
    private val refreshKey:Key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecretKey)) }
    private val accessTokenValidity:Long = 3600000L
    private val refreshTokenValidity: Long = 604800000L

    fun resolveToken(request: HttpServletRequest):String? {
        val authorizationHeader: String = request.getHeader("Authorization") ?: return null

        return authorizationHeader.takeIf { it.startsWith("Bearer ") }?.substring(7)
    }

    fun generateAccessToken(authentication: Authentication): String{
        return generateToken(authentication,accessTokenValidity,accessKey,"accessToken")
    }

    fun generateRefreshToken(authentication: Authentication):String{
        val refreshToken:String = generateToken(authentication, refreshTokenValidity,refreshKey,"refreshToken")
        val refreshTokenEntity:RefreshToken = RefreshToken(
            authentication.name,refreshToken,refreshTokenValidity
        )
        refreshTokenRepository.save(refreshTokenEntity)
        return refreshToken
    }

    private fun generateToken(authentication: Authentication, tokenExpiration: Long,key: Key,tokenType:String):String{
        val now = Date()
        val expiration = Date(now.time + tokenExpiration)

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(tokenType,true)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun accessTokenValidateToken(accessToken:String): Boolean{
        return validateToken(accessToken,accessKey)
    }

    fun refreshTokenValidateToken(refreshToken: String):Boolean{
        return validateToken(refreshToken,refreshKey)
    }

    private fun validateToken(token: String,key: Key): Boolean{
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .body
            return true

        }catch (e: SecurityException){
            logger.error("Invalid JWT signature: ${e.message}")
        }catch (e: ExpiredJwtException){
            logger.error("Expired JWT token: ${e.message}")
        }catch (e: UnsupportedJwtException){
            logger.error("Unsupported Jwt token: ${e.message}")
        }catch (e: IllegalArgumentException){
            logger.error("Jwt claims string is empty: ${e.message}")
        }
        return false
    }

    private fun parseClaims(token: String,key: Key): Claims{
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun getEmail(accessToken:String): String = parseClaims(token = accessToken, key = accessKey).subject

    fun getExpirationDate(token: String): Date = parseClaims(token,accessKey).expiration

    fun getAuthentication(accessToken:String):Authentication{
        val user: User = userRepository.findByEmail(getEmail(accessToken))
            ?: throw UsernameNotFoundException("User not found")

        val customUserDetails = CustomUserDetails(user)
        return UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.authorities)
    }
}