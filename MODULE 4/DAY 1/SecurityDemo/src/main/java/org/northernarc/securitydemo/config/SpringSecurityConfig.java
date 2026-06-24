package org.northernarc.securitydemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Bean
//    public UserDetailsService userDetailsService(){
//        UserDetails user1= User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("password1"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin=User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("password2"))
//                .roles("ADMIN","USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user1,admin);
//    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        return httpSecurity.csrf((csrf)->csrf.disable())
                .authorizeHttpRequests(auth->auth

                        .requestMatchers("/hello").permitAll()
                        .anyRequest().authenticated()
                )
//                .formLogin(Customizer.withDefaults())
//                .logout(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();

    }

}
