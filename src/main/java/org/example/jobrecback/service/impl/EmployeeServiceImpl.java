package org.example.jobrecback.service.impl;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.jobrecback.dao.EducationExperienceRepository;
import org.example.jobrecback.dao.EmployeeRepository;
import org.example.jobrecback.pojo.EducationExperience;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private EducationExperienceRepository educationExperienceRepository;

    @Override
    public Long findIdByUserId(Long userId) {
        return employeeRepository.findIdByUserId(userId);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }

    @Override
    public Employee findByUserId(Long userId) {
        return employeeRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public String uploadEmployee(Employee employee, Long userId) {
        try{
            Employee employee1 = employeeRepository.findByUserId(userId);
            if(employee1 != null){
                //更新
                System.out.println("开始更新");
                employee1.setUserId(userId);
                employee1.setAddress(employee.getAddress());
                employee1.setAge(employee.getAge());
                employee1.setAdvantage(employee.getAdvantage());
                employee1.setEmail(employee.getEmail());
                employee1.setGender(employee.getGender());
                employee1.setAwardTag(employee.getAwardTag());
                employee1.setCityId(employee.getCityId());
                employee1.setWechat(employee.getWechat());
                employee1.setDateOfBirth(employee.getDateOfBirth());
                employee1.setRealName(employee.getRealName());
                employee1.setUserPhone(employee.getUserPhone());
                employee1.setQqNumber(employee.getQqNumber());
                employee1.setSkillTag(employee.getSkillTag());
                employee1.setPersonalityTag(employee.getPersonalityTag());
                employee1.setWorkExperienceYear(employee.getWorkExperienceYear());
                employee1.setUpdateTime(Instant.now());
                //根据employeeId在EducationExperiences找到所有的行,简单粗暴先删除已经存在的教育经历,在插入新的教育经历

                List<EducationExperience> existingEducationExperiences = educationExperienceRepository.findByEmployeeId(employee1.getId());
                List<EducationExperience> newEducationExperiences = employee.getEducationExperiences();
                newEducationExperiences.forEach(educationExperience -> {
                            educationExperience.setCreateTime(Instant.now());
                            educationExperience.setUpdateTime(Instant.now());
                            educationExperience.setEmployee(employee1);
                        }
                );

                // 执行新增、更新和删除操作
                educationExperienceRepository.deleteAll(existingEducationExperiences);
                employee1.setEducationExperiences(newEducationExperiences);
                employeeRepository.save(employee1);
                System.out.println("更新成功");
                return "更新成功";
            }else{
                //新增
                System.out.println("开始新增");
                employee.setUserId(userId);
                employee.setCreateTime(Instant.now());
                employee.setUpdateTime(Instant.now());
                List<EducationExperience> educationExperiences = employee.getEducationExperiences();
                educationExperiences.forEach(educationExperience -> {
                    educationExperience.setCreateTime(Instant.now());
                    educationExperience.setUpdateTime(Instant.now());
                    educationExperience.setEmployee(employee);
                    }
                );
                employeeRepository.save(employee);
                System.out.println("新增成功");
                return "新增成功";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "操作失败，原因："+e.getMessage();
        }
    }

    @Override
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}

