package com.example.realworld.security.token

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class CustomJwtGrantedAuthoritiesConverter
    : Converter<Jwt, MutableCollection<GrantedAuthority>> {

    override fun convert(jwt: Jwt): MutableCollection<GrantedAuthority> {
        val grantedAuthorities: MutableCollection<GrantedAuthority> = ArrayList()
        for (authority in getRoles(jwt) + getScopes(jwt)) {
            grantedAuthorities.add(SimpleGrantedAuthority(authority))
        }
        return grantedAuthorities
    }

    private fun getRoles(jwt: Jwt): List<String> {
        return AuthorityUtils.createAuthorityList(jwt.getClaimAsString("role"))
            .map { "ROLE_$it" }
    }

    private fun getScopes(jwt: Jwt): List<String> {
        return AuthorityUtils.createAuthorityList(jwt.getClaimAsString("scope"))
            .map { "SCOPE_$it" }
    }
}