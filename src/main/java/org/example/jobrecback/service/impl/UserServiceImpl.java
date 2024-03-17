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
}
