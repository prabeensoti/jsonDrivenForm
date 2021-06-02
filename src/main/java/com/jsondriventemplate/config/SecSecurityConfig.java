package com.jsondriventemplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomAuthenticationProvider authProvider;

    public SecSecurityConfig(CustomAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().addHeaderWriter(
                new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**","/js/**","/images/**").permitAll()
                .antMatchers("/templates/**").hasRole("NOT_PERMITTED")
                .antMatchers("/admin/**","/preview/**").hasRole("SUPER_ADMIN")
                .antMatchers("/auth/*").hasRole("USER")
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .successHandler(myAuthenticationSuccessHandler())
                .permitAll()
                .and().logout().deleteCookies().logoutSuccessUrl("/").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }
}