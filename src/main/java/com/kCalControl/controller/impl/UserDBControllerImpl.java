package com.kCalControl.controller.impl;

import com.kCalControl.config.Checker;
import com.kCalControl.controller.UserDBController;
import com.kCalControl.dto.*;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.AssetsRepository;
import com.kCalControl.repository.BMDataRepository;
import com.kCalControl.repository.UserDBRepository;
import com.kCalControl.service.UserDBService;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class UserDBControllerImpl implements UserDBController {

    private static final Logger logger = Logger.getLogger(UserDBControllerImpl.class.getName());
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserDBRepository userDBRepository;
    @Autowired
    AssetsRepository assetsRepository;
    @Autowired
    BMDataRepository bmDataRepository;
    @Autowired
    Checker checker;
    @Autowired
    UserDBService userDBService;

    @Override
    public void createAdminUser(@RequestParam("id") ObjectId id, @RequestParam("role") String role, NewUserDTO dto, Model model, HttpServletResponse response){
//        if(!checker.checkRoleAdminById(id, model)){
//            return "error/403";
//        }
        UserDB newUserDB = userDBService.newUser(id, dto, role);
        bmDataRepository.save(newUserDB.getBmData());
        assetsRepository.save(newUserDB.getAssets());
        userDBRepository.save(newUserDB);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    public String createNormalUser(@RequestParam("id") ObjectId id, NewUserDTO dto, Model model){
        UserDB newUserDB = userDBService.newUser(id, dto, "USER");
        bmDataRepository.save(newUserDB.getBmData());
        assetsRepository.save(newUserDB.getAssets());
        userDBRepository.save(newUserDB);
        return "/views/home";
    }

    @Override
    public String myProfile(Model model){
        UserDB returnedUser = userDBService.returnLoggedUser();
        model.addAttribute("user", returnedUser);
        return "/api/myProfile";
    }

    @Override
    public String editUser(ObjectId id, Model model, Principal principal) {
        if (!checker.checkSameUser(principal, id, model)) {
            if (!checker.checkRoleAdminByPrincipal(principal, model)) {
                return "error/403";
            }
        }
        UserDB returnedUser = userDBService.returnUserById(id);
        model.addAttribute("user", returnedUser);
        model.addAttribute("userData", new UpdateUserDataDTO());
        model.addAttribute("personalData", new UpdatePersonalDataDTO());
        model.addAttribute("password", new UpdatePasswordDTO());
        return "/api/editUser";
    }

    @Override
    public void deleteUser(ObjectId id, HttpServletResponse response){
        userDBService.deleteUser(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    public void updateUserData(ObjectId id, UpdateUserDataDTO dto, Model model, HttpServletResponse response) {
        UserDB moddedUser = userDBService.returnUserById(id);
        UserDB modificationUser = userDBService.returnLoggedUser();

        moddedUser.setFirstName(dto.getFirstName());
        moddedUser.setLastName(dto.getLastName());
        moddedUser.setMobile(dto.getMobile());
        moddedUser.setEmail(dto.getEmail());
        moddedUser.setModificationPerson(modificationUser);
        moddedUser.setModificationDate(LocalDateTime.now());

        assetsRepository.save(moddedUser.getAssets());
        userDBRepository.save(moddedUser);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    public void updatePassword(ObjectId id, UpdatePasswordDTO dto, Model model, HttpServletResponse response) {
        UserDB moddedUser = userDBService.returnUserById(id);
        UserDB modificationUser = userDBService.returnLoggedUser();

        moddedUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        moddedUser.setModificationPerson(modificationUser);
        moddedUser.setModificationDate(LocalDateTime.now());

        assetsRepository.save(moddedUser.getAssets());
        userDBRepository.save(moddedUser);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    public String getUsersList(int page, int pageSize, Model model){
        Page<UserDB> usersList = userDBService.getUsers(page, pageSize);
        model.addAttribute("users", usersList.getContent());
        model.addAttribute("last", usersList.isLast());
        model.addAttribute("params",new SearchParamsDTO());
        return "/admin/listUser";
    }

    @Override
    public String searchUsers(int page, int pageSize, SearchParamsDTO dto, Model model, HttpServletResponse response) {
        Page<UserDB> userSearchList = userDBService.getUsersFromSearch(page, pageSize, dto.getQuery(), dto.getFilter(), dto.getSort());
        model.addAttribute("users", userSearchList.getContent());
        model.addAttribute("params",new SearchParamsDTO());
        return "/admin/listUser";
    }


}
