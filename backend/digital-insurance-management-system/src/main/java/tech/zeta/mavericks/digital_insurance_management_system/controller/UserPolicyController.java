package tech.zeta.mavericks.digital_insurance_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.PolicyStatusRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.request.UserPolicyRequest;
import tech.zeta.mavericks.digital_insurance_management_system.DTO.response.UserPolicyResponse;
import tech.zeta.mavericks.digital_insurance_management_system.enums.PolicyStatus;
import tech.zeta.mavericks.digital_insurance_management_system.service.UserPolicyService;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/user")
public class UserPolicyController {

    @Autowired private UserPolicyService userPolicyService;

    @PostMapping("/policy/purchase")
    public ResponseEntity<UserPolicyResponse> saveUserPolicy(@RequestBody UserPolicyRequest request){
        return new ResponseEntity<>(userPolicyService.saveUserPolicy(request), HttpStatus.CREATED);
    }

    @GetMapping("/policies/{userId}")
    public ResponseEntity<List<UserPolicyResponse>> getUserPoliciesByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(userPolicyService.getUserPoliciesByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/policy/{id}")
    public ResponseEntity<UserPolicyResponse> getUserPolicyById(@PathVariable Long id){
        return new ResponseEntity<>(userPolicyService.getUserPolicyById(id), HttpStatus.OK);
    }

    @PatchMapping("/policy/ncb/{id}")
    public ResponseEntity<UserPolicyResponse> updateUserPolicyById(@PathVariable Long id){
        return new ResponseEntity<>(userPolicyService.updateUserPolicyById(id),HttpStatus.OK);
    }

    @PatchMapping("/policy/status/{id}")
    public ResponseEntity<UserPolicyResponse> updateUserPolicyStatusById(
            @PathVariable Long id,
            @RequestBody PolicyStatusRequest request) {
        return new ResponseEntity<>(
                userPolicyService.updateUserPolicyStatusById(id, request.getPolicyStatus(),request.getPremiumRate()),
                HttpStatus.OK
        );
    }

}
