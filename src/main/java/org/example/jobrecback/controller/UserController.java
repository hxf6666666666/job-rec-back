package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.UserService;
import org.example.jobrecback.utils.ResponseUtils;
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

    //添加浏览历史
    @PostMapping("/history/{userId}/{recruitmentId}")
    public void addHistory(@PathVariable("userId")Long userId,@PathVariable("recruitmentId")Long recruitmentId) {
        userService.addHistory(userId,recruitmentId);
    }

    //添加收藏夹
    @PostMapping("/favorites/{userId}/{recruitmentId}")
    public ResponseEntity<String> addFavorites(@PathVariable("userId")Long userId,@PathVariable("recruitmentId")Long recruitmentId) {
        return ResponseUtils.response(userService::addFavorites,userId,recruitmentId);
    }
    //投递offer
    @PostMapping("/applications/{userId}/{recruitmentId}")
    public ResponseEntity<String> addApplications(@PathVariable("userId")Long userId,@PathVariable("recruitmentId")Long recruitmentId) {
        return ResponseUtils.response(userService::addApplications,userId,recruitmentId);
    }
    //获取我的浏览历史
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<Recruitment>> getHistory(@PathVariable("userId")Long userId) {
        return ResponseUtils.response(userService::getHistory,userId);
    }
    //获取我的收藏列表
    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<Recruitment>> getFavorites(@PathVariable("userId")Long userId) {
        return ResponseUtils.response(userService::getFavorites,userId);
    }
    //获取我的投递列表
    @GetMapping("/applications/{userId}")
    public ResponseEntity<List<Recruitment>> getApplications(@PathVariable("userId")Long userId) {
        return ResponseUtils.response(userService::getApplications,userId);
    }
    //获取我的offer（被同意的）
    @GetMapping("/myOffers/{userId}")
    public ResponseEntity<List<Recruitment>> getMyOffers(@PathVariable("userId")Long userId) {
        return ResponseUtils.response(userService::getMyOffers,userId);
    }
    //获取投递者的相关信息
    @GetMapping("/offers/{recruitmentId}")
    public ResponseEntity<List<Employee>> getOffers(@PathVariable("recruitmentId")Long recruitmentId){
        return ResponseUtils.response(userService::getOffers,recruitmentId);
    }
    //发放offer
    @PutMapping("/offers/{userId}/{recruitmentId}")
    public ResponseEntity<String> distributeOffer(@PathVariable("userId") Long userId,@PathVariable("recruitmentId") Long recruitmentId){
        return ResponseUtils.response(userService::distributeOffer,userId,recruitmentId);
    }
    //获取入选人员的相关信息
    @GetMapping("/winners/{recruitmentId}")
    public ResponseEntity<List<Employee>> getWinners(@PathVariable("recruitmentId")Long recruitmentId){
        return ResponseUtils.response(userService::getWinners,recruitmentId);
    }

}