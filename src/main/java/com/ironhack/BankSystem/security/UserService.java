package com.ironhack.BankSystem.security;

import com.ironhack.BankSystem.model.User;
import com.ironhack.BankSystem.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) throw new UsernameNotFoundException("User: " + username + " does not exist");
        User user = userOptional.get();

        List<GrantedAuthority> roles = new ArrayList<>();
        user.getRoles().forEach((role -> roles.add(new SimpleGrantedAuthority("ROLE_"+role.getName()  ))));

        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), roles);
    }
}
