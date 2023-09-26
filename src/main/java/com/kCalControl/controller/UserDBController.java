package com.kCalControl.controller;

import com.kCalControl.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api")
public interface UserDBController {
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("auth/admin/addUser")
    void createAdminUser(@RequestParam("id") ObjectId id, @RequestParam("role") String role, NewUserDTO dto, Model model, HttpServletResponse response);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("auth/admin/listUser")
    String getUsersList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int pageSize, Model model); //these values have lower priority than the JS ones

    //TODO fix pagination: page entity adds the search result to the existing pageable
    //TODO check parameters of JS functions passed to the server. Probably I have to include searchParams info into requests
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("auth/admin/listUser")
    String searchUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int pageSize,
                       SearchParamsDTO dto, Model model, HttpServletResponse response);

//    @PostMapping("signup")
//    void createNormalUser(@RequestBody NewUserDTO dto);

    @GetMapping("person/whoiam")
    @ResponseBody
    ResponseEntity<String> whoIAm();

    @GetMapping("auth/api/editUser/{id}")
    String editUser(@PathVariable("id") ObjectId id, Model model, Principal principal);

    @GetMapping("auth/api/deleteUser/{id}")
    void deleteUser(@PathVariable("id") ObjectId id, HttpServletResponse response);

    @PostMapping("auth/api/updateUserData/{id}")
    void updateUserData(@PathVariable("id") ObjectId id, UpdateUserDataDTO dto, Model model, HttpServletResponse response);
    @PostMapping("auth/api/updatePassword/{id}")
    void updatePassword(@PathVariable("id") ObjectId id, UpdatePasswordDTO dto, Model model, HttpServletResponse response);
}
