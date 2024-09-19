package com.example.todoapp.entity

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("refresh")
data class RefreshToken(
    @Id
    private val id: String,

    val refreshToken:String,

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    val expiration: Long,
)
