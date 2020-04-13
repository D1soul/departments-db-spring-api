package ru.d1soul.departments.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.d1soul.departments.service.authentification.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
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
        httpSecurity.csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/index.html", "/", "/login", "/register", "/logout").permitAll()
                .antMatchers("/users", "/users/{username}").permitAll()
                .antMatchers("/departments-app/main_departments").permitAll()
                        .anyRequest().authenticated()
                        .and().formLogin()
                     //   .loginPage("/login")
                        .failureUrl("/login?error=true")
                    //    .defaultSuccessUrl("http://localhost:8080/register")
                        .and().logout().logoutUrl("/logout");
    }


/*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/api/auth/login").permitAll().antMatchers("/api/auth/register").permitAll()
                .antMatchers("/api/products/**").hasAuthority("ADMIN").anyRequest().authenticated().and().csrf()
                .disable().exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint()).and()
                .apply(new JwtConfigurer(jwtTokenProvider));
        http.cors();
    }
 */

}