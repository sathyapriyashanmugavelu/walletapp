package com.wallet.walletapp.user.avatar;

import com.cloudinary.Cloudinary;
import java.io.File;
import java.io.IOException;
import java.util.Map;
public class CloudinaryService {

    public String getFile(String avatarName)
    {
        Cloudinary cloudinary=CloudinarySetup.getCloudinary();
        String imagetag=cloudinary.url().imageTag(avatarName);
        return imagetag;
    }
}

