package tech.zeta.mavericks.digital_insurance_management_system.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String greet(HttpServletRequest request){
        return "Welcome to InsureCore"+request.getSession().getId();
    }
}
