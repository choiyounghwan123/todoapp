package com.example.todoapp.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long = 0,

    @Column(nullable = false, unique = true, length = 50)
    var username:String = "",

    @Column(nullable = false, unique = true, length = 100)
    var email:String = "",

    @Column(nullable = false, length = 255)
    var password:String = ""
)