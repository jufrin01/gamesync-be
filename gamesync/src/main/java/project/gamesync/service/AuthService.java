package project.gamesync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.gamesync.dto.request.LoginRequest;
import project.gamesync.dto.request.RegisterRequest;
import project.gamesync.dto.response.JwtResponse;
import project.gamesync.entity.Role;
import project.gamesync.entity.User;
import project.gamesync.entity.UserStats;
import project.gamesync.repository.UserRepository;
import project.gamesync.repository.UserStatsRepository;
import project.gamesync.security.jwt.JwtUtils;
import project.gamesync.security.services.UserDetailsImpl;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserStatsRepository userStatsRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    // --- LOGIN ---
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        // 1. Cek Username & Password ke Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // 2. Set Authentication di Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate Token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 4. Ambil UserDetails untuk data response
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // 5. Ambil data User asli dari DB untuk info tambahan (guildId, role)
        User user = userRepository.findById(userDetails.getId()).orElse(null);
        String role = (user != null) ? user.getRole().name() : "USER";
        Long guildId = (user != null) ? user.getGuildId() : null;

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role,
                guildId);
    }

    // --- REGISTER ---
    @Transactional // Pakai Transactional biar kalau gagal simpan stats, user juga batal disimpan
    public void registerUser(RegisterRequest signUpRequest) throws Exception {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new Exception("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new Exception("Error: Email is already in use!");
        }

        // 1. Buat User Baru
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        // Set Role Default (USER)
        user.setRole(Role.USER);

        // 2. Simpan User ke DB
        User savedUser = userRepository.save(user);

        // 3. Inisialisasi User Stats (Level 1, XP 0)
        UserStats stats = new UserStats();
        stats.setUserId(savedUser.getId()); // Set ID Manual sesuai User ID
        stats.setTotalXp(0L);
        stats.setQuestsCompleted(0);

        userStatsRepository.save(stats);
    }
}