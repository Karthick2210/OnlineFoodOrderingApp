package com.initial.Service;


import com.initial.Repository.UserRepository;
import com.initial.model.USER_ROLE;
import com.initial.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emial) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(emial);

        if (user==null){
        throw new UsernameNotFoundException("user not found with this email"+user);
        }
        USER_ROLE role= user.getRole();
        List<GrantedAuthority> authorities  = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);

    }
}

