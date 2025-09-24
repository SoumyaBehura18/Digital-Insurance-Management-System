package tech.zeta.mavericks.digital_insurance_management_system.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.LoginRequest;
import tech.zeta.mavericks.digital_insurance_management_system.Service.JWTService;
import tech.zeta.mavericks.digital_insurance_management_system.Service.UserService;
import tech.zeta.mavericks.digital_insurance_management_system.model.UserPrincipal;
import tech.zeta.mavericks.digital_insurance_management_system.model.Users;

import javax.naming.AuthenticationException;
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
    public Users register(@RequestBody Users user){
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