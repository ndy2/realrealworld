package com.example.realworld

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
class RealworldApplication

fun main(args: Array<String>) {
    runApplication<RealworldApplication>(*args)
}
