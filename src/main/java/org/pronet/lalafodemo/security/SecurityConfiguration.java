package org.pronet.lalafodemo.security;

import org.pronet.lalafodemo.enums.AuthenticationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private AuthenticationSuccessHandlerImplementation authenticationSuccessHandlerImplementation;

    @Autowired
    private AuthenticationFailureHandlerImplementation authenticationFailureHandlerImplementation;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImplementation();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security)
            throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AuthenticationTypeMatcher.NON_AUTH_ROUTES)
                        .permitAll())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AuthenticationTypeMatcher.ADMIN_AUTH_ROUTES)
                        .hasAuthority(AuthenticationType.Admin.name()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AuthenticationTypeMatcher.CUSTOMER_AUTH_ROUTES)
                        .hasAuthority(AuthenticationType.Customer.name()))
                .authorizeHttpRequests(request -> request
                        .requestMatchers(AuthenticationTypeMatcher.ADMIN_AND_CUSTOMER_AUTH_ROUTES)
                        .hasAnyAuthority(AuthenticationType.Admin.name(), AuthenticationType.Customer.name()))
                .formLogin(form -> form
                        .loginPage("/auth/login-view")
                        .loginProcessingUrl("/login")
                        .successHandler(authenticationSuccessHandlerImplementation)
                        .failureHandler(authenticationFailureHandlerImplementation)
                )
                .logout(LogoutConfigurer::permitAll);
        return security.build();
    }
}
