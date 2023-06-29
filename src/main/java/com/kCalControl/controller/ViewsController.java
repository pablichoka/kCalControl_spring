package com.kCalControl.controller;

import com.kCalControl.config.Checker;
import com.kCalControl.dto.NewUserDTO;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.RoleRepository;
import com.kCalControl.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
public class ViewsController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    Checker checker;

    @GetMapping("/")
    private String index() {
        return "index";
    }

    @GetMapping("/home")
    private String home(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);

        if(checker.checkUserExistsByPrincipal(principal,model)){
            UserDB userDB = userRepository.findByUsername(principal.getName()).get();
            String fullName = userDB.getFirstName() + " "+ userDB.getLastName();
            model.addAttribute("fullName", fullName);
            model.addAttribute("user", userDB.UserDB2UserDTO());
        }else{
            return "error/404";
        }
        return "home";
    }


    @GetMapping("/adminActions/addNewUser")
    private String newUser(Principal principal, Model model){

        if(!checker.checkRoleAdminByPrincipal(principal, model)){
            return "error/403";
        }
        if(!checker.checkUserExistsByPrincipal(principal, model)){
            return "error/404";
        }
        model.addAttribute("id", userRepository.findByUsername(principal.getName()).get().getId());
        model.addAttribute("user", new NewUserDTO());
        return "adminActions/addNewUser";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

}
