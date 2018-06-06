package org.okky.reply.resource;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ClaimInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requesterId = request.getHeader("X-Requester-Id");
        String requesterGroups = request.getHeader("X-Requester-Groups");

        List<GrantedAuthority> authorities = getAuthorities(requesterGroups);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, null, authorities);
        ContextHelper.setId(requesterId);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return true;
    }

    private List<GrantedAuthority> getAuthorities(String requesterGroups) {
        if (requesterGroups == null)
            return Collections.emptyList();

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String group : requesterGroups.split(",")) {
            if (group.equals("admin")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
        }
        return authorities;
    }
}