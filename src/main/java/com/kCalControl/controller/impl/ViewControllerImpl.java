package com.kCalControl.controller.impl;

import com.kCalControl.config.Checker;
import com.kCalControl.controller.ViewController;
import com.kCalControl.dto.NewUserDTO;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.UserDBRepository;
import com.kCalControl.service.UserDBService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ViewControllerImpl implements ViewController {

    @Autowired
    UserDBRepository userDBRepository;
    @Autowired
    Checker checker;
    @Autowired
    UserDBService userDBService;
//    @Override
//    public ResponseEntity<String> index(){
//        return new ResponseEntity<String>("hello world", HttpStatus.OK);
//    }

    @Override
    public String home(Model model) {
        UserDB userDB = userDBService.returnLoggedUser();
        model.addAttribute("user", userDB);
        return "/auth/views/home";
    }

    @Override
    public String addUserFromAdmin(Model model){
        model.addAttribute("id", userDBRepository.findByUsername(userDBService.getUsernameLoggedUser()).get().getId());
        model.addAttribute("user", new NewUserDTO());
        return "/auth/admin/addNewUser";
    }

    @Override
    public String showDashboard(Model model) {
        model.addAttribute("user", userDBService.returnLoggedUser());
        return "/auth/views/dashboard";
    }

    @Override
    public String signUp(Model model) {
        model.addAttribute("user",new NewUserDTO());
        return "/noAuth/signUp";
    }
}
