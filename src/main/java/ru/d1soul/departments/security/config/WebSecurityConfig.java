package ru.d1soul.departments.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.d1soul.departments.security.jwt.JwtTokenProvider;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder managerBuilder) throws Exception{
        managerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
                    .and().apply(new JwtConfigurer(jwtTokenProvider));

        httpSecurity.authorizeRequests()
                    .antMatchers("/auth/login", "/auth/registration", "/auth/users", "/auth/forgot-password*", "/auth/reset-password*").permitAll()
                    .anyRequest().authenticated();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Пользователь не авторизован!");
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Доступ закрыт!");
    }
}