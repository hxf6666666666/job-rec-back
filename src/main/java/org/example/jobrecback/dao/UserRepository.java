package org.example.jobrecback.dao;

import org.example.jobrecback.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE (:userNickname IS NULL OR u.userNickname LIKE %:userNickname%) AND (:userRole IS NULL OR u.userRole like %:userRole%) AND (:isDisabled IS NULL OR u.isDisabled = :isDisabled) order by u.createTime desc")
    List<User> findByUserNameContainingAndUserRoleAndIsDisabled(
            @Param("userNickname") String userNickname,
            @Param("userRole") String userRole,
            @Param("isDisabled") Byte isDisabled
    );

    void deleteById(@Param("id") Long id);

    User findUserById(Long id);

    User findUserByUserName(String userName);

}
