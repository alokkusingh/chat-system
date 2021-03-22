package com.alok.tools.chat.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //.httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers("/ws", "/ws/**", "/index.html", "/")
                    .permitAll()
                .and()
                .authorizeRequests()
                    .antMatchers("/api/**")
                    .authenticated()
                    .and()
                    .httpBasic();
                //.and()
                //.anyRequest().denyAll();
    }
}
