package org.example.jobrecback.service.impl;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.jobrecback.dao.*;
import org.example.jobrecback.pojo.*;
import org.example.jobrecback.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
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
    public Page<User> findByUserNameContainingAndUserRoleIdAndIsDisabled(Pageable pageable, String userNickname, Byte userRoleId, Byte isDisabled) {
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
        return userRepository.findByUserNameContainingAndUserRoleAndIsDisabled(pageable,userNickname, userRole, isDisabled);
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

    @Resource
    private JobHistoryRepository jobHistoryRepository;
    @Override
    @Async
    public void addHistory(Long userId, Long recruitmentId) {
        try{
            JobHistory existingJobHistory = jobHistoryRepository.findByUserIdAndRecruitmentId(userId, recruitmentId);
            if(existingJobHistory != null){
                //已浏览过，更新浏览时间
                existingJobHistory.setCreateTime(Instant.now());
                jobHistoryRepository.save(existingJobHistory);
            }else{
                //没有浏览过，创建新的浏览记录
                JobHistory jobHistory = new JobHistory();
                jobHistory.setUserId(userId);
                jobHistory.setRecruitmentId(recruitmentId);
                jobHistory.setCreateTime(Instant.now());
                jobHistoryRepository.save(jobHistory);
            }
        }catch (Exception e){
            log.error("添加浏览记录失败", e);
        }
    }

    @Resource
    private JobFavoritesRepository jobFavoritesRepository;
    @Override
    public String addFavorites(Long userId, Long recruitmentId) {
        JobFavorites existingJobFavorites = jobFavoritesRepository.findByUserIdAndRecruitmentId(userId, recruitmentId);
        if(existingJobFavorites!=null){
            existingJobFavorites.setCreateTime(Instant.now());
            jobFavoritesRepository.save(existingJobFavorites);
        }else{
            JobFavorites jobFavorites = new JobFavorites();
            jobFavorites.setUserId(userId);
            jobFavorites.setRecruitmentId(recruitmentId);
            jobFavorites.setCreateTime(Instant.now());
            jobFavoritesRepository.save(jobFavorites);
        }
        return "操作成功";
    }

    @Resource
    private JobApplicationsRepository jobApplicationsRepository;
    @Override
    public String addApplications(Long userId, Long recruitmentId) {
        JobApplications existingJobApplications = jobApplicationsRepository.findByUserIdAndRecruitmentId(userId, recruitmentId);
        if(existingJobApplications!=null){
            existingJobApplications.setCreateTime(Instant.now());
            jobApplicationsRepository.save(existingJobApplications);
        }else{
            JobApplications jobApplications = new JobApplications();
            jobApplications.setUserId(userId);
            jobApplications.setRecruitmentId(recruitmentId);
            jobApplications.setOfferStatus("已投递");
            jobApplications.setCreateTime(Instant.now());
            jobApplicationsRepository.save(jobApplications);
        }
        return "操作成功";
    }
    @Resource
    private RecruitmentRepository recruitmentRepository;

    @Override
    public List<Recruitment> getHistory(Long userId) {
        // 先从JobHistory表中查到userId对应的所有recruitmentId，按照时间降序排序
        List<Long> recruitmentIds = jobHistoryRepository.findRecruitmentIdByUserId(userId);
        // 从recruitment表中按照recruitmentIds的顺序查询职位信息
        List<Recruitment> recruitmentList = new ArrayList<>();
        for (Long recruitmentId : recruitmentIds) {
            recruitmentRepository.findById(recruitmentId).ifPresent(recruitmentList::add);
        }
        return recruitmentList;
    }

    @Override
    public List<Recruitment> getFavorites(Long userId) {
        //先从JobFavorites表中查到userId对应的所有recruitmentId,再根据recruitmentId从recruitment表中查到所有职位的信息
        List<Long> recruitmentIds = jobFavoritesRepository.findRecruitmentIdByUserId(userId);
        List<Recruitment> recruitmentList = new ArrayList<>();
        for (Long recruitmentId : recruitmentIds) {
            recruitmentRepository.findById(recruitmentId).ifPresent(recruitmentList::add);
        }
        return recruitmentList;
    }

    @Override
    public List<Recruitment> getApplications(Long userId) {
        //先从JobApplications表中查到userId对应的所有recruitmentId,再根据recruitmentId从recruitment表中查到所有职位的信息
        List<Long> recruitmentIds = jobApplicationsRepository.findRecruitmentIdByUserId(userId);
        List<Recruitment> recruitmentList = new ArrayList<>();
        for (Long recruitmentId : recruitmentIds) {
            recruitmentRepository.findById(recruitmentId).ifPresent(recruitmentList::add);
        }
        return recruitmentList;
    }
    @Override
    public List<Recruitment> getMyOffers(Long userId){
        List<Long> recruitmentIds = jobApplicationsRepository.findRecruitmentIdByUserIdAndOfferStatus(userId,"已发放");
        List<Recruitment> recruitmentList = new ArrayList<>();
        for (Long recruitmentId : recruitmentIds) {
            recruitmentRepository.findById(recruitmentId).ifPresent(recruitmentList::add);
        }
        return recruitmentList;
    }
    @Resource
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getOffers(Long recruitmentId) {
        //先根据recruitmentId从JobApplications表中查到对应的所有userId,再根据userId从Employee表中查到所有应聘者信息
        List<Long> userIds = jobApplicationsRepository.findUserIdByRecruitmentId(recruitmentId);
        List<Employee> employeeList = new ArrayList<>();
        for (Long userId : userIds) {
            employeeList.add(employeeRepository.findByUserId(userId));
        }
        return employeeList;
    }

    @Override
    public String distributeOffer(Long userId, Long recruitmentId) {
        //修改JobApplications表中userId和recruitmentId对应的的offerStatus为"已发放"
        JobApplications existingJobApplication = jobApplicationsRepository.findByUserIdAndRecruitmentId(userId,recruitmentId);
        if(existingJobApplication!=null){
            existingJobApplication.setOfferStatus("已发放");
            jobApplicationsRepository.save(existingJobApplication);
            return "操作成功";
        }else return "查询不到对应offer";
    }

    @Override
    public List<Employee> getWinners(Long recruitmentId) {
        List<Long> userIds = jobApplicationsRepository.findUserIdByRecruitmentIdAndOfferStatus(recruitmentId,"已发放");
        List<Employee> employeeList = new ArrayList<>();
        for (Long userId : userIds) {
            employeeList.add(employeeRepository.findByUserId(userId));
        }
        return employeeList;
    }


}
