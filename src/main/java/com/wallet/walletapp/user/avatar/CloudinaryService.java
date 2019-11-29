package com.wallet.walletapp.user.avatar;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.wallet.walletapp.user.User;
import com.wallet.walletapp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Environment environment;

    @Autowired
    UserService userService;

    Cloudinary cloudinary;

    public CloudinaryService() {
        HashMap config = new HashMap();
        config.put("cloud_name", "your cloudinary domain name");
        config.put("api_key", "your cloudinary api key");
        config.put("api_secret", "your cloudinary api secret key");
        cloudinary = new Cloudinary(config);
    }


    public Map getMap(String name) {
        Map params = new HashMap();
        params.put("public_id", name);
        params.put("overwrite", true);
        params.put("transformation",new Transformation().width(225).height(225).crop("fit").fetchFormat("jpg"));
        return params;
    }


    public String uploadAvatar(MultipartFile avatarImage, User user) throws IOException {
        String avatarFolder=this.environment.getProperty("user.avatar.folder");
        String userName=avatarFolder+user.getUserName();
        Map uploadStatus=cloudinary.uploader().upload(avatarImage.getBytes(), getMap(userName));
        uploadStatus.get("version");

        user.setAvatarversion((Long) uploadStatus.get("version"));
        userService.updateUser(user);

        return cloudinary.url().version(user.getAvatarversion()).imageTag(userName);


    }

    public String checkAndGetTag(User user) {

        String avatarFolder=this.environment.getProperty("user.avatar.folder");
        Long avatarVersion=user.getAvatarversion();
        if(avatarVersion!=null){
            return cloudinary.url().version(avatarVersion).imageTag(avatarFolder+user.getUserName());
        }
        else{

            String avatarName=avatarFolder+this.environment.getProperty("user.avatar.defaultname");
            return cloudinary.url().imageTag(avatarName);

        }

    }
}










