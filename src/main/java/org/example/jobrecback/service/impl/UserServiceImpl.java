package org.example.jobrecback.service.impl;


import jakarta.annotation.Resource;
import org.example.jobrecback.dao.UserRepository;
import org.example.jobrecback.service.UserService;
import org.springframework.stereotype.Service;
import org.example.jobrecback.pojo.User;

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

}
