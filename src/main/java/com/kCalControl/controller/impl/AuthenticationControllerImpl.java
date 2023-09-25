package com.kCalControl.controller.impl;

import com.kCalControl.config.TokenManager;
import com.kCalControl.controller.AuthenticationController;
import com.kCalControl.dto.NewUserDTO;
import com.kCalControl.dto.auth.AuthenticateRequestDTO;
import com.kCalControl.dto.auth.AuthenticateResponseDTO;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.AssetsRepository;
import com.kCalControl.repository.BMDataRepository;
import com.kCalControl.repository.UserDBRepository;
import com.kCalControl.service.UserDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationControllerImpl implements AuthenticationController {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    UserDBRepository userDBRepository;
    @Autowired
    AssetsRepository assetsRepository;
    @Autowired
    BMDataRepository bmDataRepository;
    @Autowired
    UserDBService userDBService;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    TokenManager tokenManager;

    @Override
    public ResponseEntity<AuthenticateResponseDTO> authenticate(@RequestBody AuthenticateRequestDTO request) {

        var username = request.getUsername();
        var findByUsername = userDBRepository.findByUsername(username);
        if (findByUsername.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        }
        var userDB = findByUsername.get();

        var matches = encoder.matches(request.getPassword(), userDB.getPassword());
        if (!matches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        var userDB_id = userDB.getId().toString();
        var token = this.tokenManager.generateJwtToken(userDB_id);
        logger.debug("Generating token {} for {}", token, username);

        var response = new AuthenticateResponseDTO(userDB_id, token);
        return ResponseEntity.ok(response);
    }

    @Override
    public void createNormalUser(@RequestBody NewUserDTO dto){
        UserDB newUserDB = userDBService.newNormalUser(dto);
        bmDataRepository.save(newUserDB.getBmData());
        assetsRepository.save(newUserDB.getAssets());
        userDBRepository.save(newUserDB);
    }

}
