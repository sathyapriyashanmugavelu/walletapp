package com.wallet.walletapp.user.avatar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvatarService {
    @Autowired
    CloudinaryService cloudinaryService;

    public String getFile(String avatarName) {
        return cloudinaryService.getImageTag(avatarName);
    }
}

