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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailService customUserDetailService;
    private final CartRepository cartRepository;


    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtProvider jwtProvider,
                          CustomUserDetailService customUserDetailService,
                          CartRepository cartRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.customUserDetailService = customUserDetailService;
        this.cartRepository=cartRepository;
    }


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandle( @RequestBody User  user) throws Exception{
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        USER_ROLE role=user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist!=null) {

            throw new UsernameNotFoundException("Email Is Already Used With Another Account");
        }

     //Create new user
      User createdUser = new User();
      createdUser.setEmail(email);
      createdUser.setPassword(passwordEncoder.encode(password));
      createdUser.setFullName(fullName);
      createdUser.setRole(role);
      User saveUser =  userRepository.save(createdUser);

        Cart  cart = new Cart();
        cart.setCustomer(saveUser);
        cartRepository.save(cart);


        Authentication  authentication  = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse  authResponse =  new AuthResponse();

        authResponse.setJwt(token);

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
