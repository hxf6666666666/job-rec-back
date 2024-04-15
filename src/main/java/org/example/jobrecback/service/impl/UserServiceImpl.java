package org.example.jobrecback.service.impl;


import jakarta.annotation.Resource;
import org.example.jobrecback.dao.UserRepository;
import org.example.jobrecback.service.UserService;
import org.springframework.stereotype.Service;
import org.example.jobrecback.pojo.User;

import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    /*
     * 昵称模糊查询 + 角色精确查询 + 状态精确查询
     *
     */
    @Override
    public List<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(String userNickname, Byte userRoleId, Byte isDisabled) {
        String userRole = null;
        if(userRoleId != null) {
            if(userRoleId==0){
                userRole = "管理员";
            } else if (userRoleId==1) {
                userRole = "招聘者";
            }else if (userRoleId==2){
                userRole = "求职者";
            }
        }
        return userRepository.findByUserNameContainingAndUserRoleAndIsDisabled(userNickname, userRole, isDisabled);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User oldUser = userRepository.findUserById(id);
        if (user.getUserName() != null) {
            oldUser.setUserName(user.getUserName());
        }
        if (user.getUserRole() != null) {
            oldUser.setUserRole(user.getUserRole());
        }
        if (user.getIsDisabled() != null) {
            oldUser.setIsDisabled(user.getIsDisabled());
        }
        if (user.getUserNickname() != null) {
            oldUser.setUserNickname(user.getUserNickname());
        }
        if (user.getUserPassword() != null) {
            oldUser.setUserPassword(user.getUserPassword());
        }
        oldUser.setUpdateTime(Instant.now());
        return userRepository.save(oldUser);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }

    @Override
    public User findUserByToken(String token) {
        // 检查token格式是否正确
        if (token != null && token.startsWith("Bearer ")) {
            // 从token中提取用户ID
            String userIdString = token.substring(7).trim();
            try {
                // 将用户ID转换为Long类型
                Long userId = Long.parseLong(userIdString);
                // 根据用户ID查询用户信息
                return userRepository.findById(userId).orElse(null);
            } catch (NumberFormatException e) {
                // 如果无法解析用户ID，则返回null
                return null;
            }
        } else {
            // 如果token格式不正确，则返回null
            return null;
        }
    }

}
