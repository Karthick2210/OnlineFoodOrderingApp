package com.initial.Service;

import com.initial.Config.JwtProvider;
import com.initial.Repository.UserRepository;
import com.initial.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements  UserService{


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;


    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        System.out.println("email------------- "+email);
        User user =  userRepository.findByEmail(email);
        System.out.println("email user-------- "+user);
        if(user==null) {
            throw new BadCredentialsException("user not exist with email "+email);
        }
   	System.out.println("email user "+user.getEmail());
        return user;
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        System.out.println("email user========"+user.getEmail());
        if (user==null){
            throw  new UsernameNotFoundException("user not found");
        }
        return user;
    }
}
