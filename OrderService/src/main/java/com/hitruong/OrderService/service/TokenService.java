package com.hitruong.OrderService.service;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class TokenService {

    public String extractToken(){
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if(requestAttributes!=null){
            HttpServletRequest request = requestAttributes.getRequest();
            String authHeader = request.getHeader("Authorization");

            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                return authHeader.substring(7);
            }
        }
        return null;
    }
}
