package com.wallet.walletapp.user;

import com.wallet.walletapp.wallet.Wallet;
import com.wallet.walletapp.wallet.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Nested
    class LoadUserByUsername {
        @Test
        void shouldLoadWhenUserExists() {
            User user = new User("John", "FooBar");
            userRepository.save(user);

            UserDetails userDetails = userService.loadUserByUsername("John");
            assertEquals("John", userDetails.getUsername());
        }

        @Test
        void shouldNotLoadWhenUserDoesNotExist() {
            assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("Jane"));
        }

        @Test
        void shouldFindAnExistingUserByUsername() {
            User user = new User("John", "FooBar");
            userRepository.save(user);

            Optional<User> existingUser = userService.findUserByUsername("John");
            assertTrue(existingUser.isPresent());
            assertEquals("John", existingUser.get().getUserName());
        }

        @Test
        void shouldCreateANewUserSuccessfully() {
            UserService userService = new UserService(userRepository);

            User newUser = userService.create(new User("Test", "test123!"));

            assertNotNull(newUser);
            assertNotNull(newUser.getId());
            assertNotNull(newUser.walletId());
        }
    }
}