package com.wallet.walletapp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList()
        );
    }

    User getCurrentUser() {
        Object springUserRaw = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((org.springframework.security.core.userdetails.User)springUserRaw).getUsername();
        return findUserByUsername(username).get();
    }

    public String getUsernameForCurrentUser(){
        return getCurrentUser().getUserName();
    }

    public long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUserName(username);
                //.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User create(User user) {
        user.createWallet();
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public long getUserId(String userName){
        return userRepository.findUserId(userName);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
