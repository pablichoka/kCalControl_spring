package com.kCalControl.service.impl;

import com.kCalControl.exceptions.NetworkException;
import com.kCalControl.model.User;
import com.kCalControl.repository.UserRepository;
import com.kCalControl.service.WhoAmI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WhoAmIImpl implements WhoAmI {

    private final UserRepository userRepository;
    @Autowired
    public WhoAmIImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Integer whoAmI() {
        try {
            Integer credentials_id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName()); //returns credentials ID
            return userRepository.findByCredentialsId(credentials_id).get().getId();
        } catch (NetworkException e) {
            throw new NetworkException("User not logged", e.getHttpStatus());
        }
    }

    @Override
    public Optional<User> currentUser() {
        return userRepository.findById(whoAmI());
    }
}
