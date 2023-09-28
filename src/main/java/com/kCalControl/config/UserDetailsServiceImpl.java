package com.kCalControl.config;

import com.kCalControl.model.Role;
import com.kCalControl.model.UserDB;
import com.kCalControl.repository.UserDBRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDBRepository userDBRepository;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UserDB> userDBOptional = userDBRepository.findById(new ObjectId(id));
        UserDB userDB;
        if(userDBOptional.isPresent()){
            userDB = userDBOptional.get();
        }else{
            throw new UsernameNotFoundException(id);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<Role> roles = new ArrayList<>();
        roles.add(userDB.getRole());
        for (Role role : roles){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return new User(userDB.getUsername(), userDB.getPassword(), grantedAuthorities);
    }
}
