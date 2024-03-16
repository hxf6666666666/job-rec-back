package org.example.jobrecback.service;

import org.example.jobrecback.pojo.User;

import java.util.List;


public interface UserService {
    List<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(String userNickname, Byte userRoleId, Byte isDisabled);
}