package project.gamesync.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Pastikan import ini sesuai dengan struktur folder Anda
import project.gamesync.security.jwt.AuthEntryPointJwt;
import project.gamesync.security.jwt.AuthTokenFilter;
import project.gamesync.security.services.UserDetailsServiceImpl;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- 1. SETUP CORS (JURUS PAMUNGKAS) ---
    // Ini mengizinkan Frontend dari manapun (Localhost, Vercel, dll) untuk masuk.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Gunakan allowedOriginPatterns("*") agar fleksibel untuk semua domain
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Penting: Izinkan kredensial (Cookies/Auth Header)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // --- 2. SECURITY FILTER CHAIN (ATURAN PINTU MASUK) ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // === A. DAFTAR HALAMAN PUBLIC (Tanpa Login) ===

                        // 1. Auth (Wajib Public agar user bisa masuk)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. WebSocket (Wajib Public untuk handshake)
                        .requestMatchers("/ws/**").permitAll()

                        // 3. Data Landing Page (Agar halaman depan tidak merah/unauthorized)
                        // Sesuaikan dengan URL Controller yang Anda pakai di Frontend
                        .requestMatchers("/api/games/**").permitAll()       // Contoh: List Game
                        .requestMatchers("/api/chronicles/**").permitAll()  // Contoh: Berita/Chronicle
                        .requestMatchers("/api/guilds/**").permitAll()      // Contoh: List Guild

                        // 4. Swagger/Error (Opsional)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/error").permitAll()

                        // === B. DAFTAR HALAMAN PRIVATE (Wajib Login) ===
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}