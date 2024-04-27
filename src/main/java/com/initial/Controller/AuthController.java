package com.initial.Controller;


import com.initial.Config.JwtProvider;
import com.initial.Repository.CartRepository;
import com.initial.Repository.UserRepository;
import com.initial.Request.LoginRequest;
import com.initial.Response.AuthResponse;
import com.initial.Service.CustomUserDetailService;
import com.initial.model.Cart;
import com.initial.model.USER_ROLE;
import com.initial.model.User;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private CartRepository cartRepository;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandle(@RequestBody User  user) throws Exception{
      User email =  userRepository.findByEmail(user.getEmail());
      if (email!=null){
          throw new UsernameNotFoundException("Email is already used in another account");
      }
      User createdUser = new User();
      createdUser.setEmail(user.getEmail());
      createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
      createdUser.setFirstName(user.getFirstName());
      createdUser.setRole(user.getRole());
      User saveUser =  userRepository.save(createdUser);

        Cart  cart = new Cart();
        cart.setCustomer(saveUser);
        cartRepository.save(cart);


        Authentication  authentication  = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse  authResponse =  new AuthResponse();

        authResponse.setJwt(jwt);

        authResponse.setMessage("Registered successful");

        authResponse.setRole(user.getRole());

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest){

        String userName =  loginRequest.getEmail();
        String password =  loginRequest.getPassword();

        Authentication authentication = authenticate(userName , password);
        Collection<?extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse  authResponse =  new AuthResponse();

        authResponse.setJwt(jwt);

        authResponse.setMessage("Login successful");

        authResponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userName);
        if (userDetails==null){
            throw  new BadCredentialsException("invalid UserName.....");
        }
        if (! passwordEncoder.matches(password,userDetails.getPassword())){
            throw  new BadCredentialsException("invalid Password.....");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }


}
