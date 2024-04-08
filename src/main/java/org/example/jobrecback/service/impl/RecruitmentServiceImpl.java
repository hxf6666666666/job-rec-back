package org.example.jobrecback.service.impl;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import org.example.jobrecback.dao.RecruitmentRepository;
import org.example.jobrecback.pojo.Recruitment;
import org.example.jobrecback.service.RecruitmentService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {
    @Resource
    private RecruitmentRepository recruitmentRepository;

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
        // 创建排序对象，按照 createTime 字段倒序排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        return recruitmentRepository.findAll(spec, sort);
    }

    @Override
    public void delete(Long id) {
        recruitmentRepository.deleteById(id);
    }

    @Override
    public void update(Recruitment recruitment) {
        // 首先从数据库中获取要更新的招聘信息对象
        Optional<Recruitment> existingRecruitmentOptional = recruitmentRepository.findById(recruitment.getId());

        // 检查是否存在要更新的招聘信息对象
        if(existingRecruitmentOptional.isPresent()) {
            // 如果存在，则获取数据库中的招聘信息对象
            Recruitment existingRecruitment = existingRecruitmentOptional.get();

            // 更新数据库中的招聘信息对象的属性值
            existingRecruitment.setUserId(recruitment.getUserId());
            existingRecruitment.setJobName(recruitment.getJobName());
            existingRecruitment.setCompanyName(recruitment.getCompanyName());
            existingRecruitment.setIndustryId(recruitment.getIndustryId());
            existingRecruitment.setSalaryLower(recruitment.getSalaryLower());
            existingRecruitment.setSalaryUpper(recruitment.getSalaryUpper());
            existingRecruitment.setCity(recruitment.getCity());
            existingRecruitment.setEducationType(recruitment.getEducationType());
            existingRecruitment.setJobAddress(recruitment.getJobAddress());
            existingRecruitment.setJobDescription(recruitment.getJobDescription());
            existingRecruitment.setJobPersonality(recruitment.getJobPersonality());
            existingRecruitment.setJobSkills(recruitment.getJobSkills());
            existingRecruitment.setJobType(recruitment.getJobType());
            existingRecruitment.setLink(recruitment.getLink());
            existingRecruitment.setSalaryUnit(recruitment.getSalaryUnit());
            existingRecruitment.setWorkTimeType(recruitment.getWorkTimeType());
            existingRecruitment.setUpdateTime(Instant.now());
            // 保存更新后的招聘信息对象到数据库中
            recruitmentRepository.save(existingRecruitment);
        } else {
            // 如果要更新的招聘信息对象不存在，则抛出异常或者进行其他处理
            throw new RuntimeException("Recruitment with id " + recruitment.getId() + " not found");
        }
    }

    @Override
    public String extractEntitiesFromDescription(String description, String dictPath) throws IOException {
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


}