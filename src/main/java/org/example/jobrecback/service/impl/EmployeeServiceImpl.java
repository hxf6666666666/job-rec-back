package org.example.jobrecback.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import jakarta.annotation.Resource;
import org.example.jobrecback.dao.EducationExperienceRepository;
import org.example.jobrecback.dao.EmployeeRepository;
import org.example.jobrecback.pojo.EducationExperience;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.service.EmployeeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Resource
    private EmployeeRepository employeeRepository;
    @Resource
    private EducationExperienceRepository educationExperienceRepository;
    @Value("${hutool.AES_KEY}")
    private String AES_KEY;
    @Override
    public Long findIdByUserId(Long userId) {
        return employeeRepository.findIdByUserId(userId);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    @Override
    public List<Employee> findAllDecrypt(){
        List<Employee> employeeList = employeeRepository.findAll();
        for (Employee employee : employeeList) {
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            employee.setAddress(new String(aes.decrypt(employee.getAddress()), StandardCharsets.UTF_8));
            employee.setEmail(new String(aes.decrypt(employee.getEmail()), StandardCharsets.UTF_8));
            employee.setQqNumber(new String(aes.decrypt(employee.getQqNumber()), StandardCharsets.UTF_8));
            employee.setRealName(new String(aes.decrypt(employee.getRealName()), StandardCharsets.UTF_8));
            employee.setUserPhone(new String(aes.decrypt(employee.getUserPhone()), StandardCharsets.UTF_8));
            employee.setWechat(new String(aes.decrypt(employee.getWechat()), StandardCharsets.UTF_8));
        }
        return employeeList;
    }
    @Override
    public Page<Employee> findAllDecryptByPage(Pageable pageable){
        Page<Employee> employeeList = employeeRepository.findAllOrderByCreateTimeDesc(pageable);
        for (Employee employee : employeeList) {
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            employee.setAddress(new String(aes.decrypt(employee.getAddress()), StandardCharsets.UTF_8));
            employee.setEmail(new String(aes.decrypt(employee.getEmail()), StandardCharsets.UTF_8));
            employee.setQqNumber(new String(aes.decrypt(employee.getQqNumber()), StandardCharsets.UTF_8));
            employee.setRealName(new String(aes.decrypt(employee.getRealName()), StandardCharsets.UTF_8));
            employee.setUserPhone(new String(aes.decrypt(employee.getUserPhone()), StandardCharsets.UTF_8));
            employee.setWechat(new String(aes.decrypt(employee.getWechat()), StandardCharsets.UTF_8));
        }
        return employeeList;
    }

    @Override
    public Employee findById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.orElse(null);
    }
    @Override
    public Employee findByIdDecrypt(Long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        //对employee表中的address,email,qq_number,real_name,user_phone,wechat这几个隐私信息的字段信息解密
        if(employee.isPresent()){
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            Employee employee1 = employee.get();
            employee1.setAddress(new String(aes.decrypt(employee1.getAddress()), StandardCharsets.UTF_8));
            employee1.setEmail(new String(aes.decrypt(employee1.getEmail()), StandardCharsets.UTF_8));
            employee1.setQqNumber(new String(aes.decrypt(employee1.getQqNumber()), StandardCharsets.UTF_8));
            employee1.setRealName(new String(aes.decrypt(employee1.getRealName()), StandardCharsets.UTF_8));
            employee1.setUserPhone(new String(aes.decrypt(employee1.getUserPhone()), StandardCharsets.UTF_8));
            employee1.setWechat(new String(aes.decrypt(employee1.getWechat()), StandardCharsets.UTF_8));
            return employee1;
        }else return null;
    }

    @Override
    public Employee findByUserId(Long userId) {
        if(employeeRepository.findByUserId(userId)==null){
            Employee employee = new Employee();
            employee.setUserId(userId);
            employee.setCreateTime(Instant.now());
            employee.setUpdateTime(Instant.now());
            employeeRepository.save(employee);
        }
        return employeeRepository.findByUserId(userId);
    }
    @Override
    public Employee findByUserIdDecrypt(Long userId){
        Employee employee1 = employeeRepository.findByUserId(userId);
        if(employee1!=null){
            AES aes = SecureUtil.aes(AES_KEY.getBytes());
            employee1.setAddress(new String(aes.decrypt(employee1.getAddress()), StandardCharsets.UTF_8));
            employee1.setEmail(new String(aes.decrypt(employee1.getEmail()), StandardCharsets.UTF_8));
            employee1.setQqNumber(new String(aes.decrypt(employee1.getQqNumber()), StandardCharsets.UTF_8));
            employee1.setRealName(new String(aes.decrypt(employee1.getRealName()), StandardCharsets.UTF_8));
            employee1.setUserPhone(new String(aes.decrypt(employee1.getUserPhone()), StandardCharsets.UTF_8));
            employee1.setWechat(new String(aes.decrypt(employee1.getWechat()), StandardCharsets.UTF_8));
            return employee1;
        }else return null;
    }
    @Transactional
    @Override
    public String uploadEmployee(Employee employee, Long userId) {
        try{
            Employee employee1 = employeeRepository.findByUserId(userId);
            if(employee1 != null){
                //更新
                System.out.println(employee);
                employee1.setUserId(userId);
//                employee1.setAddress(employee.getAddress());
                employee1.setAge(employee.getAge());
                employee1.setAdvantage(employee.getAdvantage());
                if (employee.getAvatar().length() < 40000) {
                    employee1.setAvatar(employee.getAvatar());
                }
                employee1.setAwardTag(employee.getAwardTag());
                employee1.setCity(employee.getCity());
                employee1.setDateOfBirth(employee.getDateOfBirth());
//                employee1.setEmail(employee.getEmail());
                employee1.setGender(employee.getGender());
                employee1.setPersonalityTag(employee.getPersonalityTag());
//                employee1.setQqNumber(employee.getQqNumber());
//                employee1.setRealName(employee.getRealName());
                employee1.setSkillTag(employee.getSkillTag());
//                employee1.setWechat(employee.getWechat());
//                employee1.setUserPhone(employee.getUserPhone());
                employee1.setWorkExperienceYear(employee.getWorkExperienceYear());
                employee1.setUpdateTime(Instant.now());
                employee1.setEnglishTag(employee.getEnglishTag());
                employee1.setResumeIntegrity(employee.getResumeIntegrity());

                //对employee表中的date_of_birth,email,qq_number,real_name,user_phone,wechat这几个隐私信息的字段信息使用AES对称加密
                AES aes = SecureUtil.aes(AES_KEY.getBytes());
                employee1.setAddress(aes.encryptHex(employee.getAddress()));
                employee1.setEmail(aes.encryptHex(employee.getEmail()));
                employee1.setQqNumber(aes.encryptHex(employee.getQqNumber()));
                employee1.setRealName(aes.encryptHex(employee.getRealName()));
                employee1.setUserPhone(aes.encryptHex(employee.getUserPhone()));
                employee1.setWechat(aes.encryptHex(employee.getWechat()));

                //根据employeeId在EducationExperiences找到所有的行,简单粗暴先删除已经存在的教育经历,在插入新的教育经历
                List<EducationExperience> existingEducationExperiences = educationExperienceRepository.findByEmployeeId(employee1.getId());
                List<EducationExperience> newEducationExperiences = employee.getEducationExperiences();
                newEducationExperiences.forEach(educationExperience -> {
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
                //对employee表中的date_of_birth,email,qq_number,real_name,user_phone,wechat这几个隐私信息的字段信息使用AES对称加密
                AES aes = SecureUtil.aes(AES_KEY.getBytes());
                employee.setAddress(aes.encryptHex(employee.getAddress()));
                employee.setEmail(aes.encryptHex(employee.getEmail()));
                employee.setQqNumber(aes.encryptHex(employee.getQqNumber()));
                employee.setRealName(aes.encryptHex(employee.getRealName()));
                employee.setUserPhone(aes.encryptHex(employee.getUserPhone()));
                employee.setWechat(aes.encryptHex(employee.getWechat()));
                List<EducationExperience> educationExperiences = employee.getEducationExperiences();
                educationExperiences.forEach(educationExperience -> {
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

