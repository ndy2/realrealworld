package com.example.realworld.config.jwt

import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.server.resource.BearerTokenErrors
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.util.StringUtils
import java.util.regex.Pattern
import javax.servlet.http.HttpServletRequest

class CustomBearerTokenResolver : BearerTokenResolver {

    private val authorizationPattern = Pattern.compile(
        "^Token (?<token>[a-zA-Z0-9-._~+/]+=*)$",
        Pattern.CASE_INSENSITIVE
    )
    private val bearerTokenHeaderName = HttpHeaders.AUTHORIZATION


    override fun resolve(request: HttpServletRequest): String? {
        val authorization = request.getHeader(bearerTokenHeaderName)
        if (!StringUtils.startsWithIgnoreCase(authorization, "Token")) {
            return null
        }
        val matcher =  authorizationPattern.matcher(authorization)

        if (!matcher.matches()) {
            val error = BearerTokenErrors.invalidToken("Bearer token is malformed")
            throw OAuth2AuthenticationException(error)
        }
        return matcher.group("token")
    }
}