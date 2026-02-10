//package project.gamesync.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // Menerapkan aturan CORS untuk semua endpoint (/**)
//        registry.addMapping("/**")
//                // 1. Izinkan asal request (Frontend URL)
//                // Menggunakan pola wildcard agar bisa akses dari localhost port berapapun (5173, 3000, dll)
//                .allowedOriginPatterns("http://localhost:*")
//
//                // 2. Izinkan HTTP Method apa saja
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//
//                // 3. Izinkan Header apa saja (Content-Type, Authorization, dll)
//                .allowedHeaders("*")
//
//                // 4. Izinkan kredensial (Cookies / Auth Headers)
//                .allowCredentials(true)
//
//                // 5. Cache konfigurasi ini selama 1 jam (3600 detik) agar browser tidak tanya terus
//                .maxAge(3600);
//    }
//}