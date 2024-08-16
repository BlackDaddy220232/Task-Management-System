package com.application.taskmanagementsystem.service;

import com.application.taskmanagementsystem.dao.UserRepository;
import com.application.taskmanagementsystem.model.entity.User;
import com.application.taskmanagementsystem.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@CacheConfig(cacheNames = "dataUser")
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND_MESSAGE = "User with name \"%s\" already exists";
    @Autowired
    public UserService(
            UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findUserByEmail(username)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
        return UserDetailsImpl.build(user);
    }
}
