package com.example.todoapp.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
class Task (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false, length = 255)
    val title: String,

    @Column(columnDefinition = "TEXT")
    val description: String = "",

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)