package com.beanlet.web.jpa;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTimeZone;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Collection;
import java.util.List;

@Entity
public class User extends AbstractEntity<User> implements UserDetails {

  private String email;

  private String password;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  private DateTimeZone timeZone;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<UserRole> roles;

  public User() {
    // no-arg
  }

  public User(String id) {
    super(id);
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public DateTimeZone getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(DateTimeZone timeZone) {
    this.timeZone = timeZone;
  }

  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("email", email)
      .append("password", password)
      .append("accountNonExpired", accountNonExpired)
      .append("accountNonLocked", accountNonLocked)
      .append("credentialsNonExpired", credentialsNonExpired)
      .append("enabled", enabled)
      .append("roles", roles)
      .toString();
  }
}
