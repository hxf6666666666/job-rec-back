package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User user) {
        System.out.println("login");
        Map<String, Object> response = new HashMap<>();
        String username = user.getUserName();
        String password = user.getUserPassword();
        System.out.println("username:"+username+"password:"+password);
        String accessToken = authService.login(username, password);
        if (accessToken!=null) {
            response.put("accessToken", accessToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
