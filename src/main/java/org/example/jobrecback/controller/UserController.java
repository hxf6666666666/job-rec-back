package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}