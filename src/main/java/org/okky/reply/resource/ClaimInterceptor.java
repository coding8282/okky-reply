package org.okky.reply.resource;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Component
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ClaimInterceptor extends HandlerInterceptorAdapter {
    ContextHolder holder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String id = request.getHeader("X-Requester-Id");
        String groups = Objects.toString(request.getHeader("X-Requester-Groups"), "");

        List<GrantedAuthority> authorities = stream(groups.split(","))
                .map(this::toAuthority)
                .collect(toList());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id, null, authorities);
        holder.setAuthentication(authentication);
        return true;
    }

    private SimpleGrantedAuthority toAuthority(String group) {
        if (group.equals("admin")) {
            return new SimpleGrantedAuthority("ROLE_ADMIN");
        } else {
            return new SimpleGrantedAuthority("ROLE_NORMAL");
        }
    }
}