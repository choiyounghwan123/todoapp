package com.example.todoapp.entity

import jakarta.persistence.*

@Entity
@Table(name = "todos")
data class Todo(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "description", nullable = false)
    var description:String = "",

    var completed:Boolean = false

)
