package com.fyp.hotel.controller.auth;

import com.fyp.hotel.service.user.UserServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fyp.hotel.dto.login.LoginRequestDto;
import com.fyp.hotel.dto.login.LoginResponseDto;
import com.fyp.hotel.model.User;
import com.fyp.hotel.util.JwtUtils;

@RestController
@RequestMapping("/v1")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceFacade userServiceFacade;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserServiceFacade userServiceFacade) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userServiceFacade = userServiceFacade;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(
            @Validated
            @RequestBody
            LoginRequestDto request)
    {
        doAuthenticate(request.getUsername(), request.getPassword());
        final User user = (User) userServiceFacade.userAuthenticationService.loadUserByUsername(request.getUsername());

        if (user == null) {
            LoginResponseDto loginResponseDto = new LoginResponseDto();
            loginResponseDto.setUsername("");
            loginResponseDto.setToken("Invalid credentials");
            loginResponseDto.setRoleName("");
            return ResponseEntity.badRequest().body(loginResponseDto);
        } else {
            if (user.getUserStatus().equals("PENDING") || user.getUserStatus().equals("UNVERIFIED")) {
                if (user.getRoles().iterator().next().getRoleName().equals("ROLE_USER")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDto("", "user is not verified", ""));
                } else if (user.getRoles().iterator().next().getRoleName().equals("ROLE_VENDOR")) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new LoginResponseDto("", "vendor is not verified", ""));
                }
            }
                LoginResponseDto loginResponseDto = new LoginResponseDto();
                loginResponseDto.setUsername(user.getUsername());
                loginResponseDto.setToken(jwtUtils.generateToken(user));
                loginResponseDto.setRoleName(user.getRoles().iterator().next().getRoleName());
                return ResponseEntity.ok().body(loginResponseDto);
        }
    }

    private void doAuthenticate(String username, String password){
        try{
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(token);
        }
        catch(BadCredentialsException e){
            throw new BadCredentialsException ("Invalid credentials, please check your username and password");
        }
    }
    
}
