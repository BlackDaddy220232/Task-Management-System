package com.application.taskmanagementsystem.security;

import java.util.Collection;

import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.model.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private Long id;
  private String password;
  private String username;
  private Role role;

  public static UserDetailsImpl build(User user) {
    return new UserDetailsImpl(
        user.getId(), user.getPassword(), user.getUsername(),user.getRole()); //тут
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthorityUtils.createAuthorityList((getRole().toString())); //тут
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

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
}
