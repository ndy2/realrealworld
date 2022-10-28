package com.example.realworld.security.filter

import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jwt.SignedJWT
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationMacFilter(
    private val jwk: JWK
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        val token = header.replace("Bearer ", "")
        val signedJWT: SignedJWT
        try {
            signedJWT = SignedJWT.parse(token)
            val macVerifier = MACVerifier(jwk.toOctetSequenceKey().toSecretKey())
            val verify = signedJWT.verify(macVerifier)
            if (verify) {
                val jwtClaimsSet = signedJWT.jwtClaimsSet
                val userId = jwtClaimsSet.getClaim("userId") as String
                val authority = jwtClaimsSet.getClaim("authorities") as String
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                    userId.toLong(), null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authority)
                )
            }
        } catch (e: Exception) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.message)
        }
        filterChain.doFilter(request, response)
    }
}