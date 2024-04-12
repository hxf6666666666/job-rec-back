package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.service.EmployeeService;
import org.example.jobrecback.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    private EmployeeService employeeService;
    @GetMapping("/getAll")
    public ResponseEntity<List<Employee>> getAll(){
        return ResponseUtils.response(employeeService::findAll);
    }
    //根据ID返回employee表的所有信息
    @GetMapping("/getByID/{id}")
    public ResponseEntity<Employee> getByID(@PathVariable("id")Long id){
        return ResponseUtils.response(employeeService::findById,id);
    }
    //根据UserId返回表的所有信息
    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<Employee> getByUserId(@PathVariable("userId")Long userId){
        return ResponseUtils.response(employeeService::findByUserId,userId);
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
}
