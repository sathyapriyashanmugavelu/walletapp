package com.wallet.walletapp.user.avatar;

import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@WebMvcTest(AvatarService.class)

public class AvatarServiceTest {
    @MockBean
    UserService userService;

    @Autowired
    AvatarService avatarService;

    @MockBean
    CloudinaryService cloudinaryService;

    @Test
    void shouldReturnImageTagForDefaultImage() throws Exception {
        when(cloudinaryService.checkAndGetTag(any(User.class))).thenReturn("defaultimageurl");
        User user = new User(1, "test", "test123");
        String returnImage = avatarService.getAvatar(user);

        assertEquals("defaultimageurl", returnImage);
    }
}
