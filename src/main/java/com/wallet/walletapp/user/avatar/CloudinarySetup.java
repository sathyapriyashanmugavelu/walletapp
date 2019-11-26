package com.wallet.walletapp.user.avatar;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
public class CloudinarySetup {

    static Cloudinary cloudinary;

    private CloudinarySetup() {
        HashMap config=new HashMap();
        config.put("cloud_name", "dhgb2gf9a");
        config.put("api_key", "539866743742193");
        config.put("api_secret", "Ae-ZdCJla9C8bsVTPzf32kc1rxo");
        cloudinary=new Cloudinary(config);
    }
    public  static Cloudinary getCloudinary() {
        CloudinarySetup cloudinarySetup= new CloudinarySetup();
        return cloudinary;
    }

    public static Map getMap() {
        Map params = new HashMap();
        params.put("public_id","flower");
        params.put("overwrite",true);
        return params;
    }
}










