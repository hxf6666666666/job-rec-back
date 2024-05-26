package org.example.jobrecback.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.example.jobrecback.dao.EmployeeRepository;
import org.example.jobrecback.dao.JobFavoritesRecruitmentRepository;
import org.example.jobrecback.dao.RecruitmentRepository;
import org.example.jobrecback.pojo.Employee;
import org.example.jobrecback.pojo.JobFavoritesRecruitment;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.TextSimilarity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {
    @Resource
    private RecruitmentRepository recruitmentRepository;
    String dictPath1 = "@/main/resources/dict/JN.txt";
    String dictPath2 = "@/main/resources/dict/PS.txt";

    @Value("${hutool.AES_KEY}")
    private String AES_KEY;

    @Override
    public Recruitment save(Recruitment recruitment) {
        return recruitmentRepository.save(recruitment);
    }

    @Override
    public List<Recruitment> findByUserId(Long userId) {
        return recruitmentRepository.findByUserId(userId);
    }

    @Override
    public List<Recruitment> findAll() {
        return recruitmentRepository.findAll();
    }
    @Override
    public Page<Recruitment> findAllByPage(Pageable pageable) {
        return recruitmentRepository.findAll(pageable);
    }

    @Override
    public List<Recruitment> findAllByJobNameContainingAndIndustryId(String jobName, Long industryId) {
        return recruitmentRepository.findAllByJobNameContainingAndIndustryIdOrderByCreateTimeDesc(jobName, industryId);
    }

    @Override
    public Recruitment findById(Long id) {
        return recruitmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Recruitment> findAllByIndustryId(Long industryId) {
        return recruitmentRepository.findAllByIndustryId(industryId);
    }

    @Override
    public List<Recruitment> findAllByJobNameContaining(String name) {
        return recruitmentRepository.findAllByJobNameContaining(name);
    }

    @Override
    public List<Recruitment> search(String name, Integer jobType, String city, Long industryId,
                                    Byte workTimeType, Byte salary, Byte educationType) {
        Specification<Recruitment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.or(
                        cb.like(root.get("jobName"), "%" + name + "%")
                ));
            }
            if (jobType != null) {
                predicates.add(cb.equal(root.get("jobType"), jobType));
            }
            if (city != null) {
                predicates.add(cb.equal(root.get("city"), city));
            }
            if (industryId != null) {
                predicates.add(cb.equal(root.get("industryId"), industryId));
            }
            if (workTimeType != null) {
                predicates.add(cb.equal(root.get("workTimeType"), workTimeType));
            }
            //判断薪资上限大于薪资区间上限或者薪资下限小于薪资区间下限
            //salary =0 1-3K =1 3-5K =2 5-10K =3 10-20K =4 20-50K =5 50K+
            if (salary != null) {
                switch (salary) {
                    case 0 -> // 1-3K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 1),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 3)
                            ));
                    case 1 -> // 3-5K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 3),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 5)
                            ));
                    case 2 -> // 5-10K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 5),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 10)
                            ));
                    case 3 -> // 10-20K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 10),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 20)
                            ));
                    case 4 -> // 20-50K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 20),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 50)
                            ));
                    case 5 -> // 50K+
                            predicates.add(cb.greaterThanOrEqualTo(root.get("salaryUpper"), 50));
                }
            }
            if (educationType != null) {
                predicates.add(cb.equal(root.get("educationType"), educationType));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 创建排序对象，按照 createTime 字段倒序排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return recruitmentRepository.findAll(spec, sort);
    }
    @Override
    public Page<Recruitment> searchByPage(Pageable pageable,String name, Integer jobType, String city, Long industryId,
                                          Byte workTimeType, Byte salary, Byte educationType) {
        Specification<Recruitment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.or(
                        cb.like(root.get("jobName"), "%" + name + "%")
                ));
            }
            if (jobType != null) {
                predicates.add(cb.equal(root.get("jobType"), jobType));
            }
            if (city != null) {
                predicates.add(cb.equal(root.get("city"), city));
            }
            if (industryId != null) {
                predicates.add(cb.equal(root.get("industryId"), industryId));
            }
            if (workTimeType != null) {
                predicates.add(cb.equal(root.get("workTimeType"), workTimeType));
            }
            //判断薪资上限大于薪资区间上限或者薪资下限小于薪资区间下限
            //salary =0 1-3K =1 3-5K =2 5-10K =3 10-20K =4 20-50K =5 50K+
            if (salary != null) {
                switch (salary) {
                    case 0 -> // 1-3K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 1),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 3)
                            ));
                    case 1 -> // 3-5K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 3),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 5)
                            ));
                    case 2 -> // 5-10K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 5),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 10)
                            ));
                    case 3 -> // 10-20K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 10),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 20)
                            ));
                    case 4 -> // 20-50K
                            predicates.add(cb.and(
                                    cb.greaterThanOrEqualTo(root.get("salaryUpper"), 20),
                                    cb.lessThanOrEqualTo(root.get("salaryLower"), 50)
                            ));
                    case 5 -> // 50K+
                            predicates.add(cb.greaterThanOrEqualTo(root.get("salaryUpper"), 50));
                }
            }
            if (educationType != null) {
                predicates.add(cb.equal(root.get("educationType"), educationType));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 创建排序对象，按照 createTime 字段倒序排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 将排序对象和分页信息组合到一个 Pageable 对象中
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // 执行带有分页信息的查询
        return recruitmentRepository.findAll(spec, pageable);
//        return recruitmentRepository.findAll(spec, sort);
    }

    @Override
    public void delete(Long id) {
        recruitmentRepository.deleteById(id);
    }

    /**
     * 更新招聘信息
     * @param updatedRecruitment 更新后的招聘信息对象
     * @throws RuntimeException 如果指定 id 的招聘信息不存在，则抛出异常
     */
    @Override
    public void update(Recruitment updatedRecruitment) {
        Optional<Recruitment> existingRecruitmentOptional = recruitmentRepository.findById(updatedRecruitment.getId());

        if (existingRecruitmentOptional.isPresent()) {
            Recruitment existingRecruitment = existingRecruitmentOptional.get();

            // 更新数据库中的招聘信息对象的属性值，避免空指针异常和不必要的更新
            if (!StringUtils.isEmpty(updatedRecruitment.getUserId())) {
                existingRecruitment.setUserId(updatedRecruitment.getUserId());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getJobName())) {
                existingRecruitment.setJobName(updatedRecruitment.getJobName());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getCompanyName())) {
                existingRecruitment.setCompanyName(updatedRecruitment.getCompanyName());
            }
            if (updatedRecruitment.getIndustryId() != null) {
                existingRecruitment.setIndustryId(updatedRecruitment.getIndustryId());
            }
            if (updatedRecruitment.getSalaryLower() != null) {
                existingRecruitment.setSalaryLower(updatedRecruitment.getSalaryLower());
            }
            if (updatedRecruitment.getSalaryUpper() != null) {
                existingRecruitment.setSalaryUpper(updatedRecruitment.getSalaryUpper());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getCity())) {
                existingRecruitment.setCity(updatedRecruitment.getCity());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getEducationType())) {
                existingRecruitment.setEducationType(updatedRecruitment.getEducationType());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getJobAddress())) {
                existingRecruitment.setJobAddress(updatedRecruitment.getJobAddress());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getJobDescription())) {
                existingRecruitment.setJobDescription(updatedRecruitment.getJobDescription());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getJobPersonality())) {
                existingRecruitment.setJobPersonality(updatedRecruitment.getJobPersonality());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getJobSkills())) {
                existingRecruitment.setJobSkills(updatedRecruitment.getJobSkills());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getJobType())) {
                existingRecruitment.setJobType(updatedRecruitment.getJobType());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getLink())) {
                existingRecruitment.setLink(updatedRecruitment.getLink());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getSalaryUnit())) {
                existingRecruitment.setSalaryUnit(updatedRecruitment.getSalaryUnit());
            }
            if (!StringUtils.isEmpty(updatedRecruitment.getWorkTimeType())) {
                existingRecruitment.setWorkTimeType(updatedRecruitment.getWorkTimeType());
            }
            existingRecruitment.setUpdateTime(Instant.now());
            // 保存更新后的招聘信息对象到数据库中
            recruitmentRepository.save(existingRecruitment);
        } else {
            throw new RuntimeException("Recruitment with id " + updatedRecruitment.getId() + " not found");
        }
    }

    @Override
    public String extractEntitiesFromDescription(String description, String dictPath, int flag) throws IOException {
        List<String> dict = loadDictionary(dictPath);
//        System.out.println(dict);
        List<String> words = convertTermsToStrings(HanLP.segment(description));

//        System.out.println(words);

        List<String> entities = new ArrayList<>();

        for (String word : words) {
            if (word.matches("^[a-zA-Z]+$")&&flag==1||dict.contains(word)) {
                entities.add(word);
            }
        }

        return String.join(",", entities);
    }

    private List<String> loadDictionary(String dictPath) throws IOException {
        List<String> dict = new ArrayList<>();
//        try (CSVReader reader = new CSVReader(new FileReader(dictPath))) {
//            String[] line;
//            while (true) {
//                try {
//                    if ((line = reader.readNext()) == null) break;
//                } catch (IOException | CsvValidationException e) {
//                    throw new RuntimeException(e);
//                }
//                dict.add(line[0]); // 假设 CSV 文件中每行只有一个词，存储在第一个元素中
//            }
//        }
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(dictPath)) {
            assert inputStream != null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    dict.add(line);
                }
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

    @Override
    public List<Recruitment> getMyPosts(Long userId, String name, Long industryId) {
        Specification<Recruitment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("jobName"), "%" + name + "%"));
            }
            if (industryId != null) {
                predicates.add(cb.equal(root.get("industryId"), industryId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return recruitmentRepository.findAll(spec);
    }
    @Override
    public Page<Recruitment> search2(String name, Integer jobType, String city, Long industryId,
                                     Byte workTimeType, Byte salary, Byte educationType,
                                     int page, int pageSize) {
        Specification<Recruitment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.or(
                        cb.like(root.get("jobName"), "%" + name + "%")
                ));
            }
            if (jobType != null) {
                predicates.add(cb.equal(root.get("jobType"), jobType));
            }
            if (city != null) {
                predicates.add(cb.equal(root.get("city"), city));
            }
            if (industryId != null) {
                predicates.add(cb.equal(root.get("industryId"), industryId));
            }
            if (workTimeType != null) {
                predicates.add(cb.equal(root.get("workTimeType"), workTimeType));
            }
            if (salary != null) {
                switch (salary) {
                    case 0: // 1-3K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 1),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 3)
                        ));
                        break;
                    case 1: // 3-5K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 3),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 5)
                        ));
                        break;
                    case 2: // 5-10K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 5),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 10)
                        ));
                        break;
                    case 3: // 10-20K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 10),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 20)
                        ));
                        break;
                    case 4: // 20-50K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 20),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 50)
                        ));
                        break;
                    case 5: // 50K+
                        predicates.add(cb.greaterThanOrEqualTo(root.get("salaryUpper"), 50));
                        break;
                }
            }
            if (educationType != null) {
                predicates.add(cb.equal(root.get("educationType"), educationType));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 创建分页对象，根据页码和每页大小进行分页查询
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));

        // 执行带分页的查询
        return recruitmentRepository.findAll(spec, pageable);
    }

    @Override
    public List<Recruitment> recommend(Employee employee, String name, Integer jobType, String city, Long industryId, Byte workTimeType, Byte salary, Byte educationType) {
        Specification<Recruitment> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("jobName"), "%" + name + "%"));
            }
            if (jobType != null) {
                predicates.add(cb.equal(root.get("jobType"), jobType));
            }
            if (city != null) {
                predicates.add(cb.equal(root.get("city"), city));
            }
            if (industryId != null) {
                predicates.add(cb.equal(root.get("industryId"), industryId));
            }
            if (workTimeType != null) {
                predicates.add(cb.equal(root.get("workTimeType"), workTimeType));
            }
            if (salary != null) {
                switch (salary) {
                    case 0: // 1-3K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 1),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 3)
                        ));
                        break;
                    case 1: // 3-5K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 3),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 5)
                        ));
                        break;
                    case 2: // 5-10K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 5),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 10)
                        ));
                        break;
                    case 3: // 10-20K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 10),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 20)
                        ));
                        break;
                    case 4: // 20-50K
                        predicates.add(cb.and(
                                cb.greaterThanOrEqualTo(root.get("salaryUpper"), 20),
                                cb.lessThanOrEqualTo(root.get("salaryLower"), 50)
                        ));
                        break;
                    case 5: // 50K+
                        predicates.add(cb.greaterThanOrEqualTo(root.get("salaryUpper"), 50));
                        break;
                }
            }
            if (educationType != null) {
                predicates.add(cb.equal(root.get("educationType"), educationType));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 查询匹配的招聘职位
        List<Recruitment> matchedRecruitments = recruitmentRepository.findAll(spec);


        // 计算匹配度评分并排序
        List<RecruitmentWithMatchScore> recruitmentWithScores = calculateMatchScores(employee, matchedRecruitments);
        recruitmentWithScores.sort(Comparator.comparingDouble(RecruitmentWithMatchScore::getMatchScore).reversed());

        // 提取排序后的招聘职位列表的前30个职位
        List<Recruitment> top30Recruitments = recruitmentWithScores.stream()
                .limit(30) // 限制结果数量为前30个
                .map(RecruitmentWithMatchScore::getRecruitment) // 提取Recruitment对象
                .collect(Collectors.toList()); // 收集结果到列表

// 随机打乱前30个职位的顺序
        Collections.shuffle(top30Recruitments);

// 从打乱后的列表中选择前15个职位，如果不足15个，则返回整个列表
        List<Recruitment> randomRecruitments = top30Recruitments.size() < 15 ?
                top30Recruitments :
                top30Recruitments.subList(0, 15);

        return randomRecruitments;
//        // 随机排序匹配的招聘职位
//        Random random = new Random();
//        List<Recruitment> randomMatchedRecruitments = matchedRecruitments.stream()
//                .sorted((a, b) -> random.nextInt(3) - 1) // 随机排序
//                .toList();
//
//        // 提取随机排序后的前15个招聘职位
//        int numberOfRecommendations = 15;
//
//        return randomMatchedRecruitments.stream()
//                .limit(numberOfRecommendations) // 限制只取前15个
//                .collect(Collectors.toList());
    }


    // 计算匹配度评分并返回带有评分的招聘职位列表
    private List<RecruitmentWithMatchScore> calculateMatchScores(Employee employee, List<Recruitment> recruitments) {
        List<RecruitmentWithMatchScore> result = new ArrayList<>();
        for (Recruitment recruitment : recruitments) {
            double matchScore = calculateMatchScore(employee, recruitment);
            result.add(new RecruitmentWithMatchScore(recruitment, matchScore));
        }
        return result;
    }

    // 计算单个招聘职位与求职者的匹配度评分
    private double calculateMatchScore(Employee employee, Recruitment recruitment) {
        // 根据需求，编写匹配度计算逻辑，例如根据求职者的偏好和招聘职位的要求计算匹配度分数
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
        // 可根据其他条件继续计算匹配度
        return matchScore;
    }

    // 包装类，用于存储招聘职位及其匹配度评分
    private static class RecruitmentWithMatchScore {
        private Recruitment recruitment;
        private double matchScore;

        public RecruitmentWithMatchScore(Recruitment recruitment, double matchScore) {
            this.recruitment = recruitment;
            this.matchScore = matchScore;
        }

        public Recruitment getRecruitment() {
            return recruitment;
        }

        public double getMatchScore() {
            return matchScore;
        }
    }
    @Resource
    JobFavoritesRecruitmentRepository jobFavoritesRecruitmentRepository;
    @Override
    public String addFavorites(Long userId, Long recruitmentId) {
        JobFavoritesRecruitment existingJobFavorites = jobFavoritesRecruitmentRepository.findByUserIdAndRecruitmentId(userId, recruitmentId);
        if(existingJobFavorites!=null){
            existingJobFavorites.setCreateTime(Instant.now());
            jobFavoritesRecruitmentRepository.save(existingJobFavorites);
        }else{
            JobFavoritesRecruitment jobFavoritesRecruitment = new JobFavoritesRecruitment();
            jobFavoritesRecruitment.setUserId(userId);
            jobFavoritesRecruitment.setRecruitmentId(recruitmentId);
            jobFavoritesRecruitment.setCreateTime(Instant.now());
            jobFavoritesRecruitmentRepository.save(jobFavoritesRecruitment);
        }
        return "操作成功";
    }
    @Transactional
    @Override
    public String deleteFavorites(Long userId, Long recruitmentId){
        try{
            long l = jobFavoritesRecruitmentRepository.deleteByUserIdAndRecruitmentId(userId, recruitmentId);
            if(l>0)return "操作成功";
            else return "操作失败";
        }catch (Exception e){
            return "操作失败："+e.getMessage();
        }
    }
    @Resource
    public EmployeeRepository employeeRepository;
    @Override
    public List<Employee> getFavorites(Long recruitmentId) {
        List<Long> userIds = jobFavoritesRecruitmentRepository.findUserIdByRecruitmentId(recruitmentId);
        List<Employee> employeeList = new ArrayList<>();
        for (Long userId : userIds){
            Employee employee = employeeRepository.findByUserId(userId);
            try{
                encryptEmployee(employee);
                employeeList.add(employee);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return employeeList;
    }
    @Override
    public String isFavorites(Long userId, Long recruitmentId){
        JobFavoritesRecruitment existingJobFavorites = jobFavoritesRecruitmentRepository.findByUserIdAndRecruitmentId(userId, recruitmentId);
        return existingJobFavorites!=null?"已收藏":"未收藏";
    }

    @Override
    public Long countAll() {
        return recruitmentRepository.count();
    }

    public void getEmployeeDecrypt(Employee employee) {
        AES aes = SecureUtil.aes(AES_KEY.getBytes());
        employeeDecrypt(aes, employee);
    }
    public void employeeDecrypt(AES aes, Employee employee1) {
        if (employee1.getAddress() != null) {
            employee1.setAddress(new String(aes.decrypt(employee1.getAddress()), StandardCharsets.UTF_8));
        }
        if (employee1.getEmail() != null) {
            employee1.setEmail(new String(aes.decrypt(employee1.getEmail()), StandardCharsets.UTF_8));
        }
        if (employee1.getQqNumber() != null) {
            employee1.setQqNumber(new String(aes.decrypt(employee1.getQqNumber()), StandardCharsets.UTF_8));
        }
        if (employee1.getRealName() != null) {
            employee1.setRealName(new String(aes.decrypt(employee1.getRealName()), StandardCharsets.UTF_8));
        }
        if (employee1.getUserPhone() != null) {
            employee1.setUserPhone(new String(aes.decrypt(employee1.getUserPhone()), StandardCharsets.UTF_8));
        }
        if (employee1.getWechat() != null) {
            employee1.setWechat(new String(aes.decrypt(employee1.getWechat()), StandardCharsets.UTF_8));
        }
    }
    public void encryptEmployee(Employee employee){
        getEmployeeDecrypt(employee);
        if (employee.getAddress() != null) {
            employee.setAddress("已隐藏");
        }else employee.setAddress("暂无");
        if (employee.getEmail() != null) {
            employee.setEmail("已隐藏");
        }else employee.setEmail("暂无");
        if (employee.getQqNumber() != null) {
            employee.setQqNumber("已隐藏");
        }else employee.setQqNumber("暂无");
        String realName = employee.getRealName();
        if (realName != null && !realName.isEmpty()) {
            String masked = realName.charAt(0) +
                    "*".repeat(realName.length() - 1);
            employee.setRealName(masked);
        }else employee.setRealName("暂无");
        if (employee.getUserPhone() != null) {
            if(employee.getUserPhone().length() == 11){
                String encryptedPhone = employee.getUserPhone().substring(0, 3) + "****" + employee.getUserPhone().substring(employee.getUserPhone().length() - 4);
                employee.setUserPhone(encryptedPhone);
            }else if(employee.getUserPhone().length() == 8){
                String encryptedPhone = employee.getUserPhone().substring(0, 4) + "****";
                employee.setUserPhone(encryptedPhone);
            }else employee.setUserPhone("暂无");
        }else employee.setUserPhone("暂无");
        if (employee.getWechat() != null) {
            employee.setWechat("已隐藏");
        }else employee.setWechat("暂无");
        if (employee.getDateOfBirth() !=null && !employee.getDateOfBirth().isEmpty()){
            if(employee.getDateOfBirth().length()>=4){
                String dateOfBirth = employee.getDateOfBirth();
                String maskedDate = dateOfBirth.substring(0, 4)+"年"; // 只取前四个字符，即年份
                employee.setDateOfBirth(maskedDate);
            }else employee.setDateOfBirth("暂无");
        }else employee.setDateOfBirth("暂无");
    }
}