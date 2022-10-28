package com.example.realworld.controller.user

import com.example.realworld.exception.BadCredentialsException
import com.example.realworld.exception.DuplicatedEmailException
import com.example.realworld.exception.NotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.http.HttpStatus.*
import javax.validation.ConstraintViolationException


@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): Any {
        return mapOf("errors" to ex.message)
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DuplicatedEmailException::class)
    fun handleDuplicatedEmail(ex: DuplicatedEmailException): Any {
        return mapOf("errors" to "email is duplicated")
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): Any {
        return mapOf("errors" to ex.message)
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleInvalid(ex: ConstraintViolationException): Any {
        val errors = mutableMapOf<String, MutableList<String>>()
        ex.constraintViolations.forEach {
            val propertyName = it.propertyPath.toString()
            val invalidValue = it.invalidValue
            val key = "'$propertyName' is '$invalidValue'"

            if (errors.containsKey(key))
                errors[key]!!.add(it.message)
            else
                errors[key] = mutableListOf(it.message)
        }

        return mapOf("errors" to errors)
    }
}