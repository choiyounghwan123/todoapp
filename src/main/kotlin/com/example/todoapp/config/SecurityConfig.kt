package com.example.todoapp.config

import com.example.todoapp.repository.BlackListRepository
import com.example.todoapp.repository.RefreshTokenRepository
import com.example.todoapp.service.LogoutService
import com.example.todoapp.util.CustomAccessDeniedHandler
import com.example.todoapp.util.CustomAuthenticationEntryPoint
import com.example.todoapp.util.JwtUtil
import com.example.todoapp.util.filter.JwtFilter
import com.example.todoapp.util.filter.LoginFilter
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtUtil: JwtUtil,
    private val blackListRepository: BlackListRepository,
    private val objectMapper: ObjectMapper,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/logout").permitAll()
                    .requestMatchers("/api/tasks","/api/tasks/**").hasRole("USER")
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(JwtFilter(jwtUtil, blackListRepository), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAt(
                LoginFilter(authenticationManager, objectMapper, jwtUtil),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
                    .accessDeniedHandler(CustomAccessDeniedHandler())
            }
            .formLogin { it.disable() }
            .logout {
                it
                    .logoutUrl("/api/auth/logout")
                    .addLogoutHandler(
                        LogoutService(
                            jwtUtil,
                            refreshTokenRepository = refreshTokenRepository,
                            blackListRepository
                        )
                    )
                    .logoutSuccessHandler { request, response, authencation ->
                        SecurityContextHolder.clearContext()
                    }
            }
        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfigration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfigration.authenticationManager
    }
}