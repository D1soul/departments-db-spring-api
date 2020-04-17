package ru.d1soul.departments.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.d1soul.departments.security.jwt.JwtAuthenticationFilter;
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/auth/login", "/signup");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtTokenProvider);
        httpSecurity.csrf().disable();
                //    .sessionManagement()
                  //  .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                 //   .and().exceptionHandling()
                 //   .authenticationEntryPoint(unauthorizedEntryPoint())
                   // .and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.authorizeRequests()

                       // .antMatchers("/index.html", "/", "/auth/login", "/register", "/logout").permitAll()
                .antMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
                //.antMatchers("/users", "/users/{username}").permitAll()
            //    .antMatchers("/departments-app/sub-departments").permitAll()
                       // .anyRequest().authenticated()
                        .and().formLogin()
                .loginPage("/auth/login")
                      //  .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("http://localhost:8080/departments-app/main_departments")
                        .and().logout().logoutUrl("/logout");
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
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