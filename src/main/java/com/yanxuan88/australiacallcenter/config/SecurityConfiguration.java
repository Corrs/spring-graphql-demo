package com.yanxuan88.australiacallcenter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.Serializable;
import java.util.Collection;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * spring security configuration
 *
 * @author co
 * @since 2023/11/30 下午1:40:47
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {
    /**
     * todo 这里要改成从数据库获取用户 权限数据
     *
     * @return UserDetailsManager
     */
    @Bean
    public static InMemoryUserDetailsManager userDetailsService() {
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        UserDetails admin = userBuilder.username("admin").password("admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    SecurityFilterChain graphql(HttpSecurity http) throws Exception {
        return http
                // 使用token，禁用csrf
                .csrf(AbstractHttpConfigurer::disable)
                .antMatcher("/graphql/**")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                // 使用token，禁用session
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new TokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    SecurityFilterChain actuator(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/actuator/**", "/graphiql/**").hasRole("ADMIN")
                        .anyRequest().denyAll()
                )
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    DefaultMethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new PermissionEvaluator() {
            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
                // 忽略targetDomainObject
                if (authentication instanceof AusAuthenticationToken) {
                    AusAuthenticationToken token = (AusAuthenticationToken) authentication;
                    Collection<GrantedAuthority> authorities = token.getAuthorities();
                    return authorities.stream().anyMatch(ga -> {
                        AusGrantedAuthority authority = (AusGrantedAuthority) ga;
                        return authority.getPermissions().contains(String.valueOf(permission));
                    });
                }
                return false;
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                return false;
            }
        });
        return handler;
    }
}
