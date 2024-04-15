package org.example.jobrecback.service.impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.jobrecback.dao.EducationExperienceRepository;
import org.example.jobrecback.dao.EmployeeRepository;
import org.example.jobrecback.dao.RecruitmentRepository;
import org.example.jobrecback.pojo.EducationExperience;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.service.EmployeeService;
import org.example.jobrecback.service.RecruitmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.TextSimilarity;

import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Resource
    private EmployeeRepository employeeRepository;

    @Resource
    private RecruitmentService recruitmentService;

    @Resource
    private EducationExperienceRepository educationExperienceRepository;
    String dictPath1 = "src/main/resources/dict/JN.txt";
    String dictPath2 = "src/main/resources/dict/PS.txt";

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
        if(employeeRepository.findByUserId(userId)==null){
            Employee employee = new Employee();
            employee.setUserId(userId);
            employee.setCreateTime(Instant.now());
            employee.setUpdateTime(Instant.now());
            employeeRepository.save(employee);
        }
        return employeeRepository.findByUserId(userId);
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
                employee1.setAddress(employee.getAddress());
                employee1.setAge(employee.getAge());
                employee1.setAdvantage(employee.getAdvantage());
                employee1.setAvatar(employee.getAvatar());
                employee1.setAwardTag(employee.getAwardTag());
                employee1.setCity(employee.getCity());
                employee1.setDateOfBirth(employee.getDateOfBirth());
                employee1.setEmail(employee.getEmail());
                employee1.setGender(employee.getGender());
                employee1.setQqNumber(employee.getQqNumber());
                employee1.setRealName(employee.getRealName());


                if(employee1.getSkillTag()==null){
                    String JNKeywords = extractEntitiesFromDescription(employee1.getAdvantage(),dictPath1,1);
                    employee1.setSkillTag(JNKeywords);
                }else {
                    employee1.setSkillTag(employee.getSkillTag());
                }

                if(employee1.getPersonalityTag()==null){
                    String JNKeywords = extractEntitiesFromDescription(employee1.getAdvantage(),dictPath2,2);
                    employee1.setPersonalityTag(JNKeywords);
                }else {
                    employee1.setPersonalityTag(employee.getPersonalityTag());
                }


                employee1.setWechat(employee.getWechat());
                employee1.setUserPhone(employee.getUserPhone());
                employee1.setWorkExperienceYear(employee.getWorkExperienceYear());
                employee1.setUpdateTime(Instant.now());
                employee1.setEnglishTag(employee.getEnglishTag());
                employee1.setResumeIntegrity(employee.getResumeIntegrity());
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

    @Override
    public List<Employee> recommend(Long id) {



        try {
            // 获取指定ID的招聘信息
            Recruitment recruitment = recruitmentService.findById(id);

            // 获取所有求职者列表
            List<Employee> employeeList = employeeRepository.findAll();
            // 创建用于存储匹配度评分的集合
            List<EmployeeWithMatchScore> matchedEmployees = new ArrayList<>();

            // 遍历每个求职者，计算匹配度评分
            for (Employee employee : employeeList) {
                    double matchScore = calculateMatchScore(employee, recruitment);
                    matchedEmployees.add(new EmployeeWithMatchScore(employee, matchScore));
            }

            // 根据匹配度评分排序，选择前30个匹配度最高的求职者
            matchedEmployees.sort(Comparator.comparingDouble(EmployeeWithMatchScore::getMatchScore).reversed());

            // 提取前30个匹配度最高的求职者
            List<Employee> topMatchedEmployees = matchedEmployees.stream()
                    .limit(30)
                    .map(EmployeeWithMatchScore::getEmployee)
                    .collect(Collectors.toList());

            // 随机打乱前30名求职者的顺序
            Collections.shuffle(topMatchedEmployees);

            // 从打乱后的列表中选择前5个求职者，如果不足5个，则返回整个列表
            List<Employee> randomEmployees = topMatchedEmployees.size() < 5 ?
                    topMatchedEmployees :
                    topMatchedEmployees.subList(0, 5);
            return randomEmployees;
        }catch (Exception e){
            e.printStackTrace();
        }
        return List.of();
    }

    // 计算单个求职者与招聘信息的匹配度评分
    private double calculateMatchScore(Employee employee, Recruitment recruitment) {
        // 根据需求，编写匹配度计算逻辑，例如根据求职者的技能、经验、教育背景等与招聘信息进行匹配度计算
        // 这里只是示例，具体根据实际情况自行实现
        double matchScore = 0.0;

        TextSimilarity cosSimilarity = new CosineSimilarity();
        double score1 = cosSimilarity.getSimilarity(employee.getAdvantage(), recruitment.getJobDescription());
        double score2 = cosSimilarity.getSimilarity(employee.getSkillTag(), recruitment.getJobSkills());
        double score3 = cosSimilarity.getSimilarity(employee.getPersonalityTag(), recruitment.getJobPersonality());


        int skillMatchCount = 0;
        try {
            if(employee.getSkillTag()!=null&&recruitment.getJobSkills()!=null){
                String[] e = employee.getSkillTag().split(",");
                String[] r = recruitment.getJobSkills().split(",");
                for (String skillR : r) {
                    for (String skillE : e) {
                        if (skillR.trim().equals(skillE.trim())) {
                            skillMatchCount++;
                            break; // 找到匹配后，跳出内层循环
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        matchScore =  score1*0.2 + score2*0.75 + score3*0.05;
        // 使用匹配词语数量作为技能标签匹配度的一部分
        matchScore += skillMatchCount; // 可根据需要调整权重
//        System.out.println(recruitment.getJobName() + skillMatchCount);
//        System.out.println(matchScore);

        // 在这里添加根据职位需求与求职者信息进行匹配的代码，计算匹配度评分

        return matchScore;
    }

    // 包装类，用于存储求职者及其匹配度评分
    private static class EmployeeWithMatchScore {
        private Employee employee;
        private double matchScore;

        public EmployeeWithMatchScore(Employee employee, double matchScore) {
            this.employee = employee;
            this.matchScore = matchScore;
        }

        public Employee getEmployee() {
            return employee;
        }

        public double getMatchScore() {
            return matchScore;
        }
    }

    public String extractEntitiesFromDescription(String description, String dictPath, int flag) throws IOException {
        List<String> dict = loadDictionary(dictPath);
        System.out.println(dict);
        List<String> words = convertTermsToStrings(HanLP.segment(description));

        System.out.println(words);

        List<String> entities = new ArrayList<>();

        for (String word : words) {
            if (dict.contains(word)) {
                entities.add(word);
            }
        }
        return String.join(",", entities);
    }



    private List<String> loadDictionary(String dictPath) throws IOException {
        List<String> dict = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(dictPath))) {
            String[] line;
            while (true) {
                try {
                    if ((line = reader.readNext()) == null) break;
                } catch (IOException | CsvValidationException e) {
                    throw new RuntimeException(e);
                }
                dict.add(line[0]); // 假设 CSV 文件中每行只有一个词，存储在第一个元素中
            }
        }
        return dict;
    }

    public static List<String> convertTermsToStrings(List<Term> terms) {
        Set<String> wordSet = new HashSet<>(); // 使用 HashSet 去重
        for (Term term : terms) {
            wordSet.add(term.word);
        }
        return new ArrayList<>(wordSet); // 将 Set 转换为 List
    }
}

