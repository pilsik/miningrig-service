package by.sivko.miningrigservice.models.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @SequenceGenerator(name = "user_gen", sequenceName = "user_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    @Length(min = 3, message = "*Your login must have at least 3 characters")
    @NotEmpty(message = "*Please provide your login")
    private String username;

    @Column(nullable = false)
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(nullable = false)
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column
    private boolean accountNonExpired = true;

    @Column
    private boolean accountNonLocked = true;

    @Column
    private boolean credentialsNonExpired = true;

    @Column
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_and_profile",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "profile_id")})
    private Set<UserProfile> userProfiles = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserProfile> getUserProfiles() {
        return userProfiles;
    }

    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }
}
