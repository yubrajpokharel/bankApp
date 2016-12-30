package com.bank.controller;

import com.bank.dao.RoleDao;
import com.bank.domain.PrimaryAccount;
import com.bank.domain.SavingAccount;
import com.bank.domain.User;
import com.bank.domain.security.UserRole;
import com.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yubraj on 12/22/16.
 */

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @RequestMapping("/")
    public String home(){
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model modal){
        User user = new User();
        modal.addAttribute("user", user);
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(@ModelAttribute("user") User user, Model model){
        if(userService.checkUserExists(user.getUsername(), user.getEmail(), user.getPhone())){
            if(userService.checkEmailExists(user.getEmail())){
                model.addAttribute("emailExists", true);
            }
            if(userService.checkUsernameExists(user.getUsername())){
                model.addAttribute("usernameExists", true);
                System.out.println("Username exists : "+ user.getUsername());
            }
            if(userService.checkUserPhone(user.getPhone())){
                model.addAttribute("phoneExists", true);
            }
            return "signup";
        }else{
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));
            userService.createUser(user, userRoles);
            return "redirect:/";
        }
    }

    @RequestMapping("/userFront")
    public String userFront(Principal principal, Model model){
        User user = userService.findByUsername(principal.getName());
        PrimaryAccount primaryAccount = user.getPrimaryAccount();
        SavingAccount savingAccount = user.getSavingAccount();

        model.addAttribute("primaryAccount", primaryAccount);
        model.addAttribute("savingsAccount", savingAccount);

        return "userFront";
    }

}
