package org.example.jobrecback.controller;

import jakarta.annotation.Resource;
import org.example.jobrecback.annotation.CacheableToJSON;
import org.example.jobrecback.annotation.CacheableToNotJSON;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.service.RecruitmentService;
import org.example.jobrecback.utils.ResponseUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
//    String dictPath1 = "src/main/resources/dict/JN.txt";
//    String dictPath2 = "src/main/resources/dict/PS.txt";
    String dictPath1 = "dict/JN.txt";
    String dictPath2 = "dict/PS.txt";

    // 发布招聘岗位
    @PostMapping("/post")
    public ResponseEntity<Map<String, Object>> post(@RequestBody Recruitment recruitment){
        Map<String, Object> response = new HashMap<>();
        // 设置招聘信息的创建和更新时间为当前时间
        Instant now = Instant.now();
        recruitment.setCreateTime(now);
        recruitment.setUpdateTime(now);
        try {
            String JNKeywords = recruitmentService.extractEntitiesFromDescription(recruitment.getJobDescription(),dictPath1,1);
            String PSKeywords = recruitmentService.extractEntitiesFromDescription(recruitment.getJobDescription(),dictPath2,2);
            recruitment.setJobSkills(JNKeywords);
            recruitment.setJobPersonality(PSKeywords);
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
    //查看所有职位
    @GetMapping("/getAll/{page}/{size}")
    public ResponseEntity<Page<Recruitment>> getAllByPage(@PathVariable int page, @PathVariable int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseUtils.response(recruitmentService::findAllByPage,pageable);
    }
    // 根据职位、公司名称，求职类型，城市和区域，职位类型，工作经验，薪资待遇，学历要求搜索职位
    @GetMapping("/search")
    public ResponseEntity<List<Recruitment>> searchRecruitment(
            @RequestParam(name = "jobName",required = false) String name,
            @RequestParam(name = "jobType",required = false) Integer jobType,
            @RequestParam(name = "city",required = false) String city,
            @RequestParam(name = "industryId",required = false) Long industryId,
            @RequestParam(name = "workTimeType",required = false) Byte workTimeType,
            @RequestParam(name = "salary",required = false) Byte salary,
            @RequestParam(name = "educationType",required = false) Byte educationType
    ) {
        System.out.println("selectPosts: "+"name:"+name+" jobType:"+jobType+" city:"+city+" industryId:"+industryId+" workTimeType:"+workTimeType+" salary:"+salary+" educationType:"+educationType);
        return ResponseUtils.response(recruitmentService::search,name,jobType,city,industryId,workTimeType,salary,educationType);
    }
    // 根据职位、公司名称，求职类型，城市和区域，职位类型，工作经验，薪资待遇，学历要求搜索职位
    @GetMapping("/search/{page}/{size}")
    public ResponseEntity<Page<Recruitment>> searchRecruitmentByPage(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @RequestParam(name = "jobName",required = false) String name,
            @RequestParam(name = "jobType",required = false) Integer jobType,
            @RequestParam(name = "city",required = false) String city,
            @RequestParam(name = "industryId",required = false) Long industryId,
            @RequestParam(name = "workTimeType",required = false) Byte workTimeType,
            @RequestParam(name = "salary",required = false) Byte salary,
            @RequestParam(name = "educationType",required = false) Byte educationType
    ) {
        System.out.println("page: "+page+" size: "+size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseUtils.response(recruitmentService::searchByPage,pageable,name,jobType,city,industryId,workTimeType,salary,educationType);
    }
    @GetMapping("/search2")
    public ResponseEntity<Page<Recruitment>> searchRecruitment2(
            @RequestParam(name = "jobName", required = false) String name,
            @RequestParam(name = "jobType", required = false) Integer jobType,
            @RequestParam(name = "city", required = false) String city,
            @RequestParam(name = "industryId", required = false) Long industryId,
            @RequestParam(name = "workTimeType", required = false) Byte workTimeType,
            @RequestParam(name = "salary", required = false) Byte salary,
            @RequestParam(name = "educationType", required = false) Byte educationType,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int pageSize
    ) {
        System.out.println("selectPosts: " +
                "name:" + name +
                " jobType:" + jobType +
                " city:" + city +
                " industryId:" + industryId +
                " workTimeType:" + workTimeType +
                " salary:" + salary +
                " educationType:" + educationType +
                " page:" + page +
                " pageSize:" + pageSize);

        Page<Recruitment> searchResult = recruitmentService.search2(
                name,
                jobType,
                city,
                industryId,
                workTimeType,
                salary,
                educationType,
                page,
                pageSize
        );

        return ResponseEntity.ok(searchResult);
    }
    //根据职位id返回职位的所有信息
//    @CacheableToJSON(cacheNames = "recruitment_info", key = "#id", time = 60)
    @GetMapping("/getByID/{id}")
    public ResponseEntity<Recruitment> getByID(@PathVariable("id") Long id){
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
        // 设置招聘信息的创建和更新时间为当前时间
        Instant now = Instant.now();
        recruitment.setUpdateTime(now);
        try {
            String JNKeywords = recruitmentService.extractEntitiesFromDescription(recruitment.getJobDescription(),dictPath1,1);
            String PSKeywords = recruitmentService.extractEntitiesFromDescription(recruitment.getJobDescription(),dictPath2,2);
            recruitment.setJobSkills(JNKeywords);
            recruitment.setJobPersonality(PSKeywords);
            return ResponseUtils.response(recruitmentService::update,recruitment);
        } catch (Exception ignored) {}
        return null;
    }
    @PostMapping("/recommend")
    public ResponseEntity<List<Recruitment>> recommendRecruitment(
            @RequestBody Employee employee,
            @RequestParam(name = "jobName",required = false) String name,
            @RequestParam(name = "jobType",required = false) Integer jobType,
            @RequestParam(name = "city",required = false) String city,
            @RequestParam(name = "industryId",required = false) Long industryId,
            @RequestParam(name = "workTimeType",required = false) Byte workTimeType,
            @RequestParam(name = "salary",required = false) Byte salary,
            @RequestParam(name = "educationType",required = false) Byte educationType
    ) {
//        System.out.println("employee"+ employee +"selectPosts: "+"name:"+name+" jobType:"+jobType+" city:"+city+" industryId:"+industryId+" workTimeType:"+workTimeType+" salary:"+salary+" educationType:"+educationType);
        return ResponseUtils.response(recruitmentService::recommend, employee, name,jobType,city,industryId,workTimeType,salary,educationType);
    }

    //招聘方将优秀招聘者添加收藏夹
    @PostMapping("/favorites/{userId}/{recruitmentId}")
    public ResponseEntity<String> addFavorites(@PathVariable("userId")Long userId,@PathVariable("recruitmentId")Long recruitmentId) {
        return ResponseUtils.response(recruitmentService::addFavorites,userId,recruitmentId);
    }
    //取消收藏
    @DeleteMapping("/favorites/{userId}/{recruitmentId}")
    public ResponseEntity<String> deleteFavorites(@PathVariable("userId")Long userId,@PathVariable("recruitmentId")Long recruitmentId) {
        System.out.println("开始取消收藏");
        return ResponseUtils.response(recruitmentService::deleteFavorites,userId,recruitmentId);
    }
    //获取招聘方的收藏列表
    @GetMapping("/favorites/{recruitmentId}")
    public ResponseEntity<List<Employee>> getFavorites(@PathVariable("recruitmentId")Long recruitmentId) {
        return ResponseUtils.response(recruitmentService::getFavorites,recruitmentId);
    }
    @GetMapping("/favorites/{userId}/{recruitmentId}")
    public ResponseEntity<String> isFavorites(@PathVariable("userId")Long userId,@PathVariable("recruitmentId")Long recruitmentId) {
        return ResponseUtils.response(recruitmentService::isFavorites,userId,recruitmentId);
    }
    @CacheableToNotJSON(cacheNames = "recruitmentCount",time = 60)
    @GetMapping("/countAll")
    public Long countAll() {
        return recruitmentService.countAll();
    }
}