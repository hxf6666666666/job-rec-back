package org.example.jobrecback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xm.Similarity;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.EditDistanceSimilarity;
import org.xm.similarity.text.TextSimilarity;
import org.xm.tendency.word.HownetWordTendency;

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


}
