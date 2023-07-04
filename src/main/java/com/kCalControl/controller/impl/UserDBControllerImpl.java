package com.kCalControl.controller.impl;

import com.kCalControl.config.Checker;
import com.kCalControl.controller.UserDBController;
import com.kCalControl.dto.NewUserDTO;
import com.kCalControl.dto.UpdatePasswordDTO;
import com.kCalControl.dto.UpdatePersonalDataDTO;
import com.kCalControl.dto.UpdateUserDataDTO;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.AssetsRepository;
import com.kCalControl.repository.UserRepository;
import com.kCalControl.service.UserDBService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class UserDBControllerImpl implements UserDBController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssetsRepository assetsRepository;
    @Autowired
    Checker checker;
    @Autowired
    UserDBService userDBService;
    @Override
    public String createAdminUser(@RequestParam("id") ObjectId id, @RequestParam("role") String role, NewUserDTO dto, Model model){
        if(!checker.checkRoleAdminById(id, model)){
            return "error/403";
        }
        UserDB newUserDB = userDBService.newUser(id, dto, role);
        assetsRepository.save(newUserDB.getAssets());
        userRepository.save(newUserDB);
        return "redirect:/home";
    }

    @Override
    public String createNormalUser(@RequestParam("id") ObjectId id, NewUserDTO dto, Model model){
        UserDB newUserDB = userDBService.newUser(id, dto, "USER");
        assetsRepository.save(newUserDB.getAssets());
        userRepository.save(newUserDB);
        return "redirect:/home";
    }

    @Override
    public String myProfile(Model model){
        UserDB returnedUser = userDBService.returnLoggedUser();
        model.addAttribute("user", returnedUser);
        return "/userActions/myProfile";
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
        return "/userActions/editUser";
    }

    @Override
    public String deleteUser(ObjectId id){
        userDBService.deleteUser(id);
        return "redirect:/home";
    }

    @Override
    public String updateUserData(ObjectId id, UpdateUserDataDTO dto, Model model) {
        UserDB moddedUser = userDBService.returnUserById(id);
        UserDB modificationUser = userDBService.returnLoggedUser();

        moddedUser.setFirstName(dto.getFirstName());
        moddedUser.setLastName(dto.getLastName());
        moddedUser.setMobile(dto.getMobile());
        moddedUser.setEmail(dto.getEmail());
        moddedUser.setModificationPerson(modificationUser);
        moddedUser.setModificationDate(LocalDateTime.now());

        assetsRepository.save(moddedUser.getAssets());
        userRepository.save(moddedUser);

        return "redirect:/";
    }

    @Override
    public String updatePersonalData(ObjectId id, UpdatePersonalDataDTO dto, Model model) {
        UserDB moddedUser = userDBService.returnUserById(id);
        UserDB modificationUser = userDBService.returnLoggedUser();

        moddedUser.setAge(dto.getAge());
        moddedUser.setHeight(dto.getHeight());
        moddedUser.setWeight(dto.getWeight());
        moddedUser.setGender(dto.getGender());

        moddedUser.setModificationPerson(modificationUser);
        moddedUser.setModificationDate(LocalDateTime.now());

        assetsRepository.save(moddedUser.getAssets());
        userRepository.save(moddedUser);

//        if (checker.checkRoleAdminById(moddedUser.getId(), model)) {
//            return "redirect:/adminActions/listUser";
//        } else {
//            return "redirect:/userActions/myProfile";
//        }

        return "redirect:/";
    }

    @Override
    public String updatePassword(ObjectId id, UpdatePasswordDTO dto, Model model) {
        UserDB moddedUser = userDBService.returnUserById(id);
        UserDB modificationUser = userDBService.returnLoggedUser();

        moddedUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        moddedUser.setModificationPerson(modificationUser);
        moddedUser.setModificationDate(LocalDateTime.now());

        assetsRepository.save(moddedUser.getAssets());
        userRepository.save(moddedUser);

        return "redirect:/";
    }

    @Override
    public String getUsersList(int page, int pageSize, Model model){
        Page<UserDB> usersList = userDBService.getUsers(page, pageSize);
        model.addAttribute("users", usersList.getContent());
        model.addAttribute("last", usersList.isLast());
        return "/userActions/listUser";
    }



}
