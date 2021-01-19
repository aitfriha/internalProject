package com.wproducts.administration.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.wproducts.administration.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.techniu.isbackend.Response;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.techniu.isbackend.exception.ValidationConstants.AUTH_WRONG_CREDENTIALS;
import static com.wproducts.administration.security.SecurityConstants.*;

public class ApiJWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public ApiJWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/auth", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {

            User user = new ObjectMapper().readValue(req.getInputStream(), User.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserEmail(),
                            user.getUserPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        if (auth.getPrincipal() != null) {

            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
            String login = user.getUsername();

            if (login != null && login.length() > 0) {
                Claims claims = Jwts.claims().setSubject(login);
                List<String> roles = new ArrayList<>();
                user.getAuthorities().forEach(authority -> roles.add(authority.getAuthority()));
                claims.put("UserRoles", roles);
                String token = Jwts.builder()
                        .setClaims(claims)
                        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                        .signWith(SignatureAlgorithm.HS512, SECRET)
                        .compact();
                res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);

                // Send token in response
                res.setStatus(HttpServletResponse.SC_OK);
                res.setContentType("application/json");
                res.setCharacterEncoding("UTF-8");

                Map<String, String> map = new HashMap<>();
                map.put("token", token);

                res.getWriter().write(new Gson().toJson(Response.ok().setPayload(map)));
                res.getWriter().flush();
            }
        }
    }

    /**
     * Send JSON response when auth is unsuccessful
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        // Send error in response
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        Map<String, String> authBodyResponse = new HashMap<>();
        authBodyResponse.put("authBody", AUTH_WRONG_CREDENTIALS);

        response.getWriter().write(new Gson().toJson(Response.wrongCredentials().setErrors(authBodyResponse)));
        response.getWriter().flush();
    }
}