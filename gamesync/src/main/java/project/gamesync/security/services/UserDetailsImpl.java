package project.gamesync.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.gamesync.entity.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    private Long id;

    private String username;

    @Getter
    private String email;

    @JsonIgnore
    private String password;

    @Getter
    private Long guildId; // Menyimpan ID Guild user

    @Getter
    private Integer level; // Menyimpan Level user
    // --------------------------------------------------

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password,
                           Long guildId, Integer level,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.guildId = guildId;
        this.level = level;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        // Mengubah Role (Enum) menjadi Authority Spring Security
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().name())
        );

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getGuildId(), // Ambil Guild ID dari Entity User
                user.getLevel(),   // Ambil Level dari Entity User
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }



    // --- Status Akun (Default True semua agar akun aktif) ---
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    // --- Equals & HashCode (Penting untuk Session Management) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}