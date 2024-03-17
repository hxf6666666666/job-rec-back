package org.example.jobrecback.service;

import org.example.jobrecback.pojo.User;

import java.util.List;


public interface UserService {
    List<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(String userNickname, Byte userRoleId, Byte isDisabled);

    void deleteById(Long id);

    User save(User user);

    User updateUser(Long id, User user);
}