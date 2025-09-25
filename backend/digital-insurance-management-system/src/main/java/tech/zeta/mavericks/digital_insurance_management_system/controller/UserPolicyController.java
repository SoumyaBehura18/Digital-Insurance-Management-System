package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.entity.UserPolicy;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserPolicyService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserPolicyController {
    @Autowired private UserPolicyService userPolicyService;

    @PostMapping("/policy/purchase")
    public ResponseEntity<UserPolicy> saveUserPolicy(@RequestBody UserPolicy userPolicy){
        return new ResponseEntity<>(userPolicyService.saveUserPolicy(userPolicy), HttpStatus.CREATED);
    }

    @GetMapping("/policies")
    public ResponseEntity<List<UserPolicy>> getUserPolcies(){
        return new ResponseEntity<>(userPolicyService.getUserPolicies(),HttpStatus.OK);
    }

    @GetMapping("/policy/{id}")
    public ResponseEntity<UserPolicy> getUserPolicyById(Long id){
        return new ResponseEntity<>(userPolicyService.getUserPolicyById(id),HttpStatus.FOUND);
    }
}
