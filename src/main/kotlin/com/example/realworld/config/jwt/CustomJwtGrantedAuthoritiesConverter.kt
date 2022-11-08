package com.example.realworld.config.jwt

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

/**
 * 커스텀 Jwt 권한 컨버터
 *
 * Jwt 객체의 claim 에서 role, scope 클래임을 추출해서 대문자 prefix 를 추가하여
 * SimpleGrantedAuthority 의 컬렉션으로 변환해준다.
 */
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