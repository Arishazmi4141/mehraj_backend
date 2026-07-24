package e_commerce.khilat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

import e_commerce.khilat.admin.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtAuthenticationFilter jwtFilter;
	
	  public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
	        this.jwtFilter = jwtFilter;
	    }


    // ✅ Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

   
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // 1. Enable CORS support
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 2. Explicitly permit OPTIONS requests for all paths
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() 
                .requestMatchers("/api/admin/login","/api/admin/signup","/api/order/create-guestorder").permitAll()
                .requestMatchers("/api/stripe/webhook").permitAll()
                .requestMatchers("/api/razorpay/webhook").permitAll()
                .requestMatchers("/api/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}