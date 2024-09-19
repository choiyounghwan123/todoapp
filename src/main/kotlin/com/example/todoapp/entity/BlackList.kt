package com.example.todoapp.entity

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import java.util.concurrent.TimeUnit

@RedisHash("black")
data class BlackList(
    @Id
    val id: String,

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    val expiration:Long
)
