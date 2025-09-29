package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.dto.LoginRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.request.RoleUpdateRequest;
import tech.zeta.mavericks.digital_insurance_management_system.dto.response.LoginResponse;
import tech.zeta.mavericks.digital_insurance_management_system.enums.RoleType;
import tech.zeta.mavericks.digital_insurance_management_system.service.JWTService;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserService;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPrincipal;
import tech.zeta.mavericks.digital_insurance_management_system.entity.User;

import java.util.List;
import java.util.Map;




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
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", false, "error", e.getMessage()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwtToken = jwtService.generateToken(userPrincipal.getUsername());

        LoginResponse responseDTO = new LoginResponse(
                userPrincipal.getId(),
                userPrincipal.getRole().toLowerCase(),
                jwtToken,
                userPrincipal.getUser().getSmokingDrinking(),
                userPrincipal.getUser().getVehicleType(),
                userPrincipal.getUser().getVehicleAge(),
                userPrincipal.getUser().getPreexistingConditions(),
                userPrincipal.getUser().getName(),
                userPrincipal.getUser().getEmail()
        );

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(service.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/getUsersByIds")
    public ResponseEntity<List<User>> getUsersByIds(@RequestParam List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return new ResponseEntity<>(List.of(), HttpStatus.OK);
            }
            List<User> users = service.getUsersByIds(ids);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/updateUserRole/{id}")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        RoleType role = RoleType.valueOf(request.getRoleType().toUpperCase());
        return new ResponseEntity<>(service.updateUserRole(id, role), HttpStatus.OK);
    }

}