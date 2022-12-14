package com.yanxin.credit.configuration;

import com.yanxin.credit.filter.JwtAuthFilter;
import com.yanxin.credit.oauth2.AuthExceptionEntryPoint;
import com.yanxin.credit.oauth2.LogoutSuccessHandlerImpl;
import com.yanxin.credit.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;

/**
 * @author sa
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private AuthExceptionEntryPoint authExceptionEntryPoint;

    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Autowired
    private CorsFilter corsFilter;

    public static String[] ignoreUrls = {"/", "/login", "/**/v2/api-docs/**", "/swagger-resources/**",
            "/swagger-ui.html", "/webjars/**", "/api/**", "/**/favicon.ico"};


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable()
                // ?????????????????????
                .exceptionHandling().authenticationEntryPoint(authExceptionEntryPoint)
                .and()
                // ????????????
                .authorizeRequests().antMatchers(ignoreUrls).permitAll()
                .and().authorizeRequests()
                .anyRequest().authenticated();
        http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        // ??????CORS filter
        http.addFilterBefore(corsFilter, JwtAuthFilter.class);
        http.addFilterBefore(corsFilter, LogoutFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService()).passwordEncoder(bCryptPasswordEncoder());

    }

    /**
     * ???????????????????????????
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }
}
