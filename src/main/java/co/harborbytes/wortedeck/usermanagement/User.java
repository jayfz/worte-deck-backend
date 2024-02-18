package co.harborbytes.wortedeck.usermanagement;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "user_email_unique", columnNames = "email")
})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 64)
    @Column(name = "first_name", length = 64)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 64)
    @Column(name = "last_name", length = 64)
    private String lastName;

    @NotNull
    @Size(min = 8, max = 64)
    private String password;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private Role role;

    @NotNull
    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired = true;

    @NotNull
    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked = true;

    @NotNull
    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired = true;

    @NotNull
    @Column(name = "is_enabled")
    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
