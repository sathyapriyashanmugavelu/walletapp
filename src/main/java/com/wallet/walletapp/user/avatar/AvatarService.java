package com.wallet.walletapp.user.avatar;

import com.wallet.walletapp.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class AvatarService {
    @Autowired
    CloudinaryService cloudinaryService;


    public String getAvatar(User user) {
        return cloudinaryService.checkAndGetTag(user);
    }

    public String uploadAvatar(MultipartFile avatarImage, User user) throws IOException {
        return cloudinaryService.uploadAvatar(avatarImage,user);
    }
}

