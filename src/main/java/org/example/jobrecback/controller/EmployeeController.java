package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.annotation.CacheableToJSON;
import org.example.jobrecback.annotation.CacheableToNotJSON;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.service.EmployeeService;
import org.example.jobrecback.utils.CacheClient;
import org.example.jobrecback.utils.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;
    @Resource
    private CacheClient cacheClient;
    @GetMapping("/crypt")
    public ResponseEntity<String> crypt(){
        return ResponseUtils.response(employeeService::crypt);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<Employee>> getAll(){
        return ResponseUtils.response(employeeService::findAll);
    }
    @GetMapping("/getAll2")
    public ResponseEntity<List<Employee>> getAll2(){
        return ResponseUtils.response(employeeService::findAllDecrypt);
    }
    @GetMapping("/getAll3/{page}/{size}")
    public ResponseEntity<Page<Employee>> getAll3(@PathVariable int page, @PathVariable int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseUtils.response(employeeService::findAllDecryptByPage,pageable);
    }
    @GetMapping("/recommend/{id}")
    public ResponseEntity<List<Employee>> recommend(@PathVariable("id") Long id){
        return ResponseUtils.response(employeeService::recommend,id);
    }

    //根据ID返回employee表的所有信息
    @GetMapping("/getByID/{id}")
    public ResponseEntity<Employee> getByID(@PathVariable("id")Long id){
        return ResponseUtils.response(employeeService::findById,id);
    }
    //根据ID返回employee表的所有信息(解密)
    @GetMapping("/getByID2/{id}")
    public ResponseEntity<Employee> getByID2(@PathVariable("id")Long id){
        return ResponseUtils.response(employeeService::findByIdDecrypt,id);
    }
    //根据UserId返回表的所有信息
    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<Employee> getByUserId(@PathVariable("userId")Long userId){
        return ResponseUtils.response(employeeService::findByUserId,userId);
    }
    //根据UserId返回表的所有信息(解密)
    @GetMapping("/getByUserId2/{userId}")
    public ResponseEntity<Employee> findByUserIdDecrypt(@PathVariable("userId")Long userId){
        return ResponseUtils.response(employeeService::findByUserIdDecrypt,userId);
    }
    //上传和更新employee表中的信息,教育经历可以有多段,所以
    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> upload(@RequestBody Employee employee,@PathVariable("userId") Long userId){
        return ResponseUtils.response(employeeService::uploadEmployee,employee,userId);
    }
    //删除employee表中的信息
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        return ResponseUtils.response(employeeService::delete,id);
    }

    @CacheableToNotJSON(cacheNames = "employeeCount",time = 60)
    @GetMapping("/countAll")
    public Long countAll() {
        return employeeService.countAll();
    }
}
