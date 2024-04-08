package org.example.jobrecback;

import org.example.jobrecback.service.RecruitmentService;
import org.example.jobrecback.service.impl.RecruitmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xm.Similarity;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.EditDistanceSimilarity;
import org.xm.similarity.text.TextSimilarity;
import org.xm.tendency.word.HownetWordTendency;

import java.io.IOException;

@SpringBootTest
class JobRecBackApplicationTests {

    @Test
    void contextLoads() {

    }

    @Test
    void test1(){
        String text1 = "任职要求：1、精通HTML/CSS/JavaScript，有良好的编码习惯，有代码洁癖者优先；" +
                "2、熟悉JS技术，包括AJAX、DOM、JSON、熟悉Vue；" +
                "3、较强的学习能力、逻辑思维能力、良好的沟通能力、团队协作精神；" +
                "4、有常见框架（uni/Element）项目经验者优先。" +
                "5、熟悉小程序，公众号开发" +
                "优先考虑招聘说明：优先考虑22年毕业生，要求有一定实践经验，肯吃苦，学习沟通能力强";
        String text2 = "Vue,沟通能力,团队协作精神。" +
                "HTML,CSS,JavaScript" +
                "uni-app/Element UI，小程序、公众号开发,AJAX、DOM、JSON";

        String text3 = "java,go语言,python,c++,c语言,23年毕业生,naive ui,后端，算法";

        TextSimilarity cosSimilarity = new CosineSimilarity();
        double score1 = cosSimilarity.getSimilarity(text1, text2);
        double score2 = cosSimilarity.getSimilarity(text1, text3);
        System.out.println("cos相似度分值：" + score1);
        System.out.println("cos相似度分值：" + score2);
    }


    @Test
    void test2(){
        String description = "适合有点基础、技术有待提高且没有项目经验的新人条件：1、思维逻辑清晰，有较好的理解和沟通协助能力。2、积极上进有责任心，有毅力，能吃苦耐劳。3、学习能力强，能快速掌握新技术，对新技术保持关注和热情。4、熟悉HTML、CSS、javaScript等基础知识优先；5、接受0基础但是努力、认真的新人。\n";
        String dictPath = "E:\\※NJUCM\\AAAAAA服创赛\\job-rec-back\\src\\main\\resources\\dict\\PS.txt";

        RecruitmentService service = new RecruitmentServiceImpl();

        try {
            String entities = service.extractEntitiesFromDescription(description, dictPath);
            System.out.println("提取的关键词为：" + entities);
        } catch (IOException e) {
            System.err.println("处理出错：" + e.getMessage());
        }
    }


}
