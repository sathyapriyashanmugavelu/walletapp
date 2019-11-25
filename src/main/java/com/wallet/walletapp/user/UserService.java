package com.wallet.walletapp.user;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    WalletService walletService;

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

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUserName(username);
                //.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User create(User user) {
        user.createWallet();
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
