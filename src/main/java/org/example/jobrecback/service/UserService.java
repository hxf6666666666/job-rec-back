package org.example.jobrecback.service;

import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {
    List<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(String userNickname, Byte userRoleId, Byte isDisabled);
    Page<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(Pageable pageable, String userNickname, Byte userRoleId, Byte isDisabled);

    void deleteById(Long id);

    User save(User user);

    User updateUser(Long id, User user);

    User findUserById(Long id);

    User findUserByToken(String token);

    void addHistory(Long userId,Long recruitmentId);

    String addFavorites(Long userId,Long recruitmentId);
    String deleteFavorites(Long userId,Long recruitmentId);
    String isFavorites(Long userId,Long recruitmentId);

    String addApplications(Long userId,Long recruitmentId);
    String deleteApplications(Long userId,Long recruitmentId);
    String isApplications(Long userId,Long recruitmentId);

    List<Recruitment> getHistory(Long userId);

    List<Recruitment> getFavorites(Long userId);

    List<Recruitment> getApplications(Long userId);
    List<Recruitment> getMyOffers(Long userId);

    List<Employee> getOffers(Long recruitmentId);

    String distributeOffer(Long userId,Long recruitmentId);


    List<Employee> getWinners(Long recruitmentId);


    List<Employee> getFavorites2(Long recruitmentId);

    List<Employee> getOffers2(Long recruitmentId);

    String isDistribute(Long userId,Long recruitmentId);

    String cancelOffer(Long userId,Long recruitmentId);
}