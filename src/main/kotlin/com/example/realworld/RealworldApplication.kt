package com.example.realworld

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@ConfigurationPropertiesScan(basePackages = ["com.example.realworld.config.properties"])
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
class RealworldApplication

fun main(args: Array<String>) {
    runApplication<RealworldApplication>(*args)
}
