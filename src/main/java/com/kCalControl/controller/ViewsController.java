package com.kCalControl.controller;

import com.kCalControl.dto.UserDTO;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.RoleRepository;
import com.kCalControl.repository.UserRepository;
import jakarta.annotation.security.RolesAllowed;
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
import java.util.Optional;


@Controller
public class ViewsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/")
    private String index() {
        return "index";
    }

    @GetMapping("/home")
    private String home(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);

        Optional<UserDB> optionalUserDB = userRepository.findByUsername(principal.getName());
        if(optionalUserDB.isPresent()){
            String fullname = optionalUserDB.get().getFirstName() + " "+ optionalUserDB.get().getLastName();
            model.addAttribute("fullname", fullname);
        }

        return "home";
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/forms/signUpForm")
    private String newUser(Model model){
        model.addAttribute("user", new UserDTO());
        return "forms/signUpForm";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

}
