package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.zeta.mavericks.digital_insurance_management_system.dto.LoginRequest;
import tech.zeta.mavericks.digital_insurance_management_system.service.JWTService;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserService;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPrincipal;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;

import java.util.HashMap;
import java.util.Map;




@CrossOrigin(origins = "http://localhost:8082") // allow your frontend
@RestController


public class UserController{
    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JWTService jwtService;

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return service.register(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        }catch(Exception e){
            Map<String, Object> map = new HashMap<>();
            map.put("error",e.getMessage());
            map.put("status", false);

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);

        }


        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwtToken = this.jwtService.generateToken(userPrincipal.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        return ResponseEntity.ok(response);

    }


}