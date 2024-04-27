package com.initial.Response;

import com.initial.model.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse   {

    private  String Jwt;

    private String message;

    private USER_ROLE role;


}
