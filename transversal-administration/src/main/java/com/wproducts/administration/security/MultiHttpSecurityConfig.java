package com.wproducts.administration.security;

import com.wproducts.administration.security.api.ApiJWTAuthenticationFilter;
import com.wproducts.administration.security.api.ApiJWTAuthorizationFilter;
import com.wproducts.administration.security.form.CustomAuthenticationSuccessHandler;
import com.wproducts.administration.security.form.CustomLogoutSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
public class MultiHttpSecurityConfig {

    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        private final CustomUserDetailsService userDetailsService;

        public ApiWebSecurityConfigurationAdapter(BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService userDetailsService) {
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            this.userDetailsService = userDetailsService;
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        // @formatter:off
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf()
                    .disable()
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .antMatchers("/api/staff/user/**").permitAll()
                    .antMatchers("/api/user/signup").permitAll()
                   // .antMatchers("/api/user/forgetPassword/**").permitAll()
                    .antMatchers("/api/user/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/translation/**").permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                    .and()
                    .addFilter(new ApiJWTAuthenticationFilter(authenticationManager()))
                    .addFilter(new ApiJWTAuthorizationFilter(authenticationManager()))
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    http.cors();
        }
        // @formatter:on

    }

    @Order(2)
    @Configuration
    public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

        private final CustomUserDetailsService userDetailsService;

        public FormLoginWebSecurityConfigurerAdapter(BCryptPasswordEncoder bCryptPasswordEncoder, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomUserDetailsService userDetailsService) {
            this.bCryptPasswordEncoder = bCryptPasswordEncoder;
            this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
            this.userDetailsService = userDetailsService;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        // @formatter:off
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors()
                    .and()
                    .csrf()
                    .disable()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/dashboard/**").hasAuthority("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .failureUrl("/login?error=true")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler)
                    .and()
                    .logout()
                    .permitAll()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                    .deleteCookies("JSESSIONID")
                    .logoutSuccessUrl("/")
                    .and()
                    .exceptionHandling();
        }


        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(
                    "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
                    "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
                    "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png",
                    "/v2/api-docs", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
                    "/webjars/**", "/swagger-resources/**", "/swagge‌​r-ui.html", "/actuator",
                    "/actuator/**");
        }

        // @formatter:on
    }
}