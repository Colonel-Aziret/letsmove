package com.letsmove.service;

import com.letsmove.entity.Users;
import com.letsmove.dao.UserRepository;
import com.letsmove.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public Users save(Users user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedDate(new Date());
        return userRepository.save(user);
    }
    public Users FindByLogin(String login){
        return userRepository.findFirstByLogin(login);
    }
    public void update(Users users) {
        this.userRepository.save(users);
    }
}