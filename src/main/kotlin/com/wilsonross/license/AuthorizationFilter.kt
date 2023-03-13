package com.wilsonross.license

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import java.util.*


class AuthorizationFilter() : GenericFilterBean() {
    @Value("\${license.token}")
    private val token: String? = null

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain
    ) {
        val header = (request as HttpServletRequest).getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer")) {
            return (response as HttpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED)
        }

        val auth = this.getAuth(request)
            ?: return (response as HttpServletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED)

        SecurityContextHolder.getContext().authentication = auth
        chain.doFilter(request, response)
    }

    private fun getAuth(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader("Authorization")
        val tokenValue = token.replace("Bearer ", "")

        val secretTokenBytes = this.token?.toByteArray()
        val secretTokenEncoded = Base64.getEncoder().encodeToString(secretTokenBytes)

        if (secretTokenEncoded != tokenValue) {
            return null
        }

        return UsernamePasswordAuthenticationToken(tokenValue, null)
    }
}