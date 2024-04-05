package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.service.RecruitmentService;
import org.example.jobrecback.utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {
    @Resource
    private RecruitmentService recruitmentService;

    // 发布招聘岗位
    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> post(@RequestBody Recruitment recruitment){
        Map<String, Object> response = new HashMap<>();
        // 设置招聘信息的创建和更新时间为当前时间
        Instant now = Instant.now();
        recruitment.setCreateTime(now);
        recruitment.setUpdateTime(now);
        try {
            // 保存招聘信息到数据库
            Recruitment savedRecruitment = recruitmentService.save(recruitment);
            response.put("status", "success");
            response.put("message", "Recruitment posted successfully.");
            response.put("data", savedRecruitment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to post recruitment due to an error: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 查看我发布的职位
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<List<Recruitment>> getMyPosts(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "industryId", required = false) Long industryId) {
        System.out.println("userId:" + userId + " name:" + name + " industryId:" + industryId);
        return ResponseUtils.response(recruitmentService::getMyPosts, userId, name, industryId);
    }
    //查看所有职位
    @GetMapping("/getAll")
    public ResponseEntity<List<Recruitment>> getAll() {
        return ResponseUtils.response(recruitmentService::findAll);
    }
    // 根据职位、公司名称，求职类型，城市和区域，职位类型，工作经验，薪资待遇，学历要求搜索职位
    @GetMapping("/search")
    public ResponseEntity<List<Recruitment>> searchRecruitment(
            @RequestParam(name = "name",required = false) String name,
            @RequestParam(name = "jobType",required = false) Integer jobType,
            @RequestParam(name = "cityId",required = false) Long cityId,
            @RequestParam(name = "industryId",required = false) Long industryId,
            @RequestParam(name = "workTimeType",required = false) Byte workTimeType,
            @RequestParam(name = "salary",required = false) Byte salary,
            @RequestParam(name = "educationType",required = false) Byte educationType
    ) {
        System.out.println("selectPosts: "+"name:"+name+" jobType:"+jobType+" cityId:"+cityId+" industryId:"+industryId+" workTimeType:"+workTimeType+" salary:"+salary+" educationType:"+educationType);
        return ResponseUtils.response(recruitmentService::search,name,jobType,cityId,industryId,workTimeType,salary,educationType);
    }
    //根据职位id返回职位的所有信息
    @GetMapping("/getByID/{id}")
    public ResponseEntity<Recruitment> getByID(@PathVariable("id") Long id){
        System.out.println("getbyid");
        return ResponseUtils.response(recruitmentService::findById, id);
    }
    //删除职位
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        return ResponseUtils.response(recruitmentService::delete,id);
    }
    //更新职位信息
    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody Recruitment recruitment){
        return ResponseUtils.response(recruitmentService::update,recruitment);
    }



}
