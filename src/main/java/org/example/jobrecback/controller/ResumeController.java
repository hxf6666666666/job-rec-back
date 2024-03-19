package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.Resume;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.ResumeService;
import org.example.jobrecback.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Resource
    private UserService userService;

    @Resource
    private ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file,
                                               @RequestHeader("Authorization") String authorizationHeader) {
        // 检查Authorization头部参数是否存在并且以Bearer开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // 获取Token
            // 根据Token查询用户信息
            User user = userService.findUserByToken(token);
            if (user != null) {
                // 保存上传的简历
                String result = resumeService.uploadResume(file, user.getId());
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("用户未认证");
            }
        } else {
            // 如果Authorization头部参数不存在或者格式不正确，返回401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("缺少身份验证信息");
        }
    }

    @GetMapping("/all")
    public List<Resume> getAllResume(@RequestParam(required = false) String fileName,
                                     @RequestParam(required = false) Integer resumeStatus,
                                     @RequestHeader("Authorization") String authorizationHeader) {

        // 检查Authorization头部参数是否存在并且以Bearer开头
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // 获取Token
            // 根据Token查询用户信息
            User user = userService.findUserByToken(token);
            if (user != null) {
                return  resumeService.findResumeAll(fileName, resumeStatus, user.getId());
            } else {
                return null;
            }
        } else {
            // 如果Authorization头部参数不存在或者格式不正确
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
    }
}
