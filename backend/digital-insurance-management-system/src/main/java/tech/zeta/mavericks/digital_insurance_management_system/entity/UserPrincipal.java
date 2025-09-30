package tech.zeta.mavericks.digital_insurance_management_system.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security UserDetails implementation for authentication.
 *
 * Wraps the User entity and provides methods required by Spring Security
 * to perform authentication and authorization checks.
 */
public class UserPrincipal implements UserDetails {

    /** The user entity being wrapped */
    private User user;

    /**
     * Constructor to create a UserPrincipal from a User entity.
     *
     * @param user the User entity
     */
    public UserPrincipal(User user) {
        this.user = user;
    }

    /** Returns the wrapped User entity */
    public User getUser() {
        return this.user;
    }

    /** Returns the authorities granted to the user (ROLE_USER by default) */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    /** Returns the password of the user */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /** Returns the username (email) of the user */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    /** Returns the role of the user as a string (ADMIN or USER) */
    public String getRole() {
        return user.getRoleType() == RoleType.ADMIN ? "ADMIN" : "USER";
    }

    /** Returns the ID of the user */
    public Long getId() {
        return user.getId();
    }
}
