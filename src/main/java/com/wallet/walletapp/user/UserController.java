package com.wallet.walletapp.user;

import com.wallet.walletapp.user.avatar.AvatarService;
import com.wallet.walletapp.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;



@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    WalletService walletService;

    @Autowired
    AvatarService avatarService;

    @Autowired
    private Environment environment;

    @RequestMapping("/signup")
    String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user/signup";
    }

    @PostMapping("/users")
    String create(@ModelAttribute User user, Model model) {
        Optional<User> userExists= userService.findUserByUsername(user.getUserName());
        if(userExists.isPresent()){
            model.addAttribute("userAlreadyAvailable","Username already in use");
            model.addAttribute("user",new User());
            return "user/signup";
        }
        userService.create(user);
        return "redirect:/login";
    }

   @RequestMapping("/userprofile")
    String get(Model model) {
        String defaultAvatar=this.environment.getProperty("user.avatar.default");
        String imageTag= avatarService.getFile(defaultAvatar);
        model.addAttribute("userName", "Sathya");
        model.addAttribute("imagetag", imageTag);
        return "user/profile";
    }
}