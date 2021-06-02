package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Test
    void save() {
        questions.save(Q1);
        questions.save(Q2);

        assertThat(questions.findAll().size()).isEqualTo(2);
    }

    @Test
    void is_owner() {
        questions.save(Q1);
        questions.save(Q2);
        //findAll은 캐시에 있는 Entity들을 가져오는게 아닌가?
        assertThat(questions.findAll().get(0).isOwner(UserTest.JAVAJIGI)).isTrue(); //쿼리실행
        assertThat(questions.findAll().get(1).isOwner(UserTest.SANJIGI)).isTrue(); //쿼리실행
    }

    @Test
    void find_by_title() {
        Question question1 = questions.save(Q1);
        Question question2 = questions.save(Q2);

        List<Question> questionList = questions.findAll(); //셀렉트 쿼리실행

        Question expected1 = questions.findByTitle("title1").get(0); //셀렉트 쿼리실행

        Question expected2 = questions.findById(expected1.getId()).get();
        Question expected3 = questions.findById(expected2.getId()).get();

        Question expected4 = questions.findById(questionList.get(1).getId()).get();
        Question expected5 = questions.findByTitle(questionList.get(1).getTitle()).get(0); //셀렉트 쿼리실행

        assertThat(question1).isSameAs(expected1);
        assertThat(question1).isSameAs(expected2);
        assertThat(question1).isSameAs(expected3);
        assertThat(question2).isSameAs(expected4);
        assertThat(question2).isSameAs(expected5);
    }
}
