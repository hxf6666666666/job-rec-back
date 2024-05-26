package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.pojo.Resume;
import org.example.jobrecback.service.EmployeeService;
import org.example.jobrecback.service.ResumeService;
import org.example.jobrecback.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/resume")
public class ResumeController {
    @Resource
    private ResumeService resumeService;
    @Resource
    private EmployeeService employeeService;

    //获取用户上传的简历信息
    @Transactional
    @GetMapping("/getByUserId/{userId}")
    public ResponseEntity<Resume> getByUserId(@PathVariable Long userId){
        try{
            Long employeeId = employeeService.findIdByUserId(userId);
            if (employeeId == null) {
                //用户还未上传简历，返回空
                Employee employee = new Employee();
                employee.setId(userId);
                employeeService.uploadEmployee(employee, userId);
//                return ResponseEntity.ok(new Resume());
            }
            Resume resume = resumeService.findByEmployeeId(employeeId);
            return ResponseEntity.ok(Objects.requireNonNullElseGet(resume, Resume::new));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //获取全部简历
    @GetMapping("/getAll")
    public  ResponseEntity<List<Resume>> getAll(){
        return ResponseUtils.response(resumeService::findAll);
    }
    @GetMapping("/getAll/{page}/{size}")
    public ResponseEntity<Page<Resume>> getAll(@PathVariable int page, @PathVariable int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseUtils.response(resumeService::findAllByPage,pageable);
    }
    //上传兼更新
    @PostMapping("/upload/{employeeId}")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,@PathVariable("employeeId") Long employeeId){
        return ResponseUtils.response(resumeService::uploadResume,file,employeeId);
    }

    //删除简历（物理删除+数据库删除）
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        return ResponseUtils.response(resumeService::delete,id);
    }

    //按条件搜索
    @GetMapping("/search")
    public ResponseEntity<List<Resume>> searchResume(
            @RequestParam(name = "fileName",required = false) String fileName,
            @RequestParam(name = "uploaderName",required = false) String uploaderName
    ) {
        System.out.println("fileName: "+fileName+" uploaderName: "+uploaderName);
        return ResponseUtils.response(resumeService::searchResume,fileName,uploaderName);
    }
}
