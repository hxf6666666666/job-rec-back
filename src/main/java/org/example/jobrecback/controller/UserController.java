package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    // 搜索用户
    @GetMapping("/all")
    public List<User> searchUsers(
            @RequestParam(required = false) String userNickname,
            @RequestParam(required = false) Byte userRoleId,
            @RequestParam(required = false) Byte isDisabled) {
        return userService.findByUserNameContainingAndUserRoleIdAndIsDisabled(userNickname, userRoleId, isDisabled);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id,user);
    }

    @GetMapping("/detail")
    public ResponseEntity<User> getUserDetail(@RequestHeader("Authorization") String authorizationHeader) {
        // 检查Authorization头部参数是否存在并且以Bearer开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // 获取Token
            // 根据Token查询用户信息
            User user = userService.findUserByToken(token);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            // 如果Authorization头部参数不存在或者格式不正确，返回401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}