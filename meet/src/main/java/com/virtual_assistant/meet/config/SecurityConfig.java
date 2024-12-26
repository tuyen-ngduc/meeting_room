
package com.virtual_assistant.meet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth
                        -> auth .requestMatchers("/**").permitAll());

        return http.build();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter(jwtUtil());
    }

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(secretKey);
    }

}

//@Configuration
//@EnableMethodSecurity // Kích hoạt @PreAuthorize và các annotation khác
//public class SecurityConfig {
//
//    private final UserDetailsService userDetailsService;
//
//    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder(); // Sử dụng BCrypt để mã hóa mật khẩu
//    }
//
//    @Bean
//    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ Admin mới được truy cập
//                        .requestMatchers("/secretary/**").hasAnyRole("ADMIN", "SECRETARY") // Admin và Thư ký có quyền
//                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "SECRETARY", "USER") // Tất cả các role được phép truy cập
//                        .anyRequest().authenticated() // Các request còn lại yêu cầu phải đăng nhập
//                )
//                .formLogin(form -> form // Cấu hình form đăng nhập
//                        .loginPage("/login") // URL của trang login tùy chỉnh (nếu có)
//                        .permitAll()
//                )
//                .logout(logout -> logout // Cấu hình logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login?logout") // Điều hướng sau khi logout
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//}



