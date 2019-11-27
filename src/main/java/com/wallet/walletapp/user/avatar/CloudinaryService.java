package com.wallet.walletapp.user.avatar;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    Cloudinary cloudinary;

    public CloudinaryService() {
        HashMap config = new HashMap();
        config.put("cloud_name", "dhgb2gf9a");
        config.put("api_key", "539866743742193");
        config.put("api_secret", "Ae-ZdCJla9C8bsVTPzf32kc1rxo");
        cloudinary = new Cloudinary(config);
    }

    Cloudinary getCloudinary() {
        CloudinaryService cloudinarySetup = new CloudinaryService();
        return cloudinary;
    }

    public Map getMap() {
        Map params = new HashMap();
        params.put("public_id", "flower");
        params.put("overwrite", true);
        return params;
    }

    public String getImageTag(String avatarName) {
        return cloudinary.url().imageTag(avatarName);
    }
}










