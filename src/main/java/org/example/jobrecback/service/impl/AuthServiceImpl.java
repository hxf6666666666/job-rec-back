package org.example.jobrecback.service.impl;

import jakarta.annotation.Resource;
import org.example.jobrecback.dao.UserRepository;
import org.example.jobrecback.pojo.User;
import org.example.jobrecback.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserRepository userRepository;

    @Override
    public String login(String username, String password) {
        User user = userRepository.findUserByUserName(username);
        if (user != null && user.getUserPassword().equals(password) && user.getIsDisabled()!=1) {
            // 返回一个 accessToken, 简单起见，直接使用用户id
            return ""+user.getId();
        }
        return null;
    }
}
