package com.orp.config;

import com.orp.services.WorkingService;
import com.orp.utils.CurrentUserUtils;
import com.orp.utils.auth.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    WorkingService workingService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/stylesheet/**", "/assets/**", "/javascript/**", "/node_modules/**", "/register").permitAll()
                                .requestMatchers("/project/**", "/staff/**", "/api/project/**").hasRole("ADMIN")
                                .requestMatchers("/claim/approved/**", "/claim/paid/**", "/claim/financeAction/**").hasRole("FINANCE")
                                .requestMatchers("/claim/pendingApproval/**", "/claim/approvedOrPaid/**", "/claim/approvalAction/**")
                                .access((authentication, object) -> {
                                    if (!authentication.get().getPrincipal().equals("anonymousUser") && authentication.get() != null && authentication.get().isAuthenticated()) {
                                        return new AuthorizationDecision(workingService.checkRecord(CurrentUserUtils.getStaffInfo().getId()));
                                    } else {
                                        return new AuthorizationDecision(false);
                                    }
                                })
                                .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin
                        .loginPage("/login").permitAll()
                        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler()))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true))
                .rememberMe(rememberMe -> rememberMe
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(86400)
                        .key("remember-me-key"));
        return http.build();
    }
}
