package com.initial.Service;


import com.initial.Repository.UserRepository;
import com.initial.model.USER_ROLE;
import com.initial.model.User;
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

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if (user!=null){

            USER_ROLE role= user.getRole();
            List<GrantedAuthority> authorities  = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role.toString()));
            return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
        }
        else {

            throw new UsernameNotFoundException("user not found with this email"+username);
        }


    }
}
