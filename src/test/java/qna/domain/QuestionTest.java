package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveAndFind() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        flushAndClear();

        Question question1 = questionRepository.findById(1L).get();
        Question question2 = questionRepository.findById(2L).get();

        assertAll(
                () -> assertThat(question1.getTitle()).isEqualTo("title1"),
                () -> assertThat(question2.getTitle()).isEqualTo("title2")
        );
    }



    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
