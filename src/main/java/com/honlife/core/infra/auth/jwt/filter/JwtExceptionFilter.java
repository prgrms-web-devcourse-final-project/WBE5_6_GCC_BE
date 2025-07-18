package com.honlife.core.infra.auth.jwt.filter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.honlife.core.infra.error.exceptions.AuthApiException;
import com.honlife.core.infra.error.exceptions.CommonException;
import com.honlife.core.infra.response.ResponseCode;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
    
    private final HandlerExceptionResolver handlerExceptionResolver;
    
    public JwtExceptionFilter(
        @Qualifier("handlerExceptionResolver")
        HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        
        try {
            filterChain.doFilter(request, response);
        } catch (CommonException ex) {
            throwAuthEx(request, response, ex.code());
        } catch (JwtException ex) {
            throwAuthEx(request, response, ResponseCode.UNAUTHORIZED);
        }
    }
    
    private void throwAuthEx(HttpServletRequest request, HttpServletResponse response, ResponseCode code) {
        handlerExceptionResolver.resolveException(request, response, null, new AuthApiException(code));
    }
}