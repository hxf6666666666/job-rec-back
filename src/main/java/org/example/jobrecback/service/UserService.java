package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.pojo.User;

import java.util.List;


public interface UserService {
    List<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(String userNickname, Byte userRoleId, Byte isDisabled);

    void deleteById(Long id);

    User save(User user);

    User updateUser(Long id, User user);

    User findUserById(Long id);

    User findUserByToken(String token);

    void addHistory(Long userId,Long recruitmentId);

    String addFavorites(Long userId,Long recruitmentId);

    String addApplications(Long userId,Long recruitmentId);

    List<Recruitment> getHistory(Long userId);

    List<Recruitment> getFavorites(Long userId);

    List<Recruitment> getApplications(Long userId);
    List<Recruitment> getMyOffers(Long userId);

    List<Employee> getOffers(Long recruitmentId);

    String distributeOffer(Long userId,Long recruitmentId);


    List<Employee> getWinners(Long recruitmentId);
}