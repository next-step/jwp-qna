package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questions;

    @Test
    public void save(){
        Question q1 = questions.save(Q1);

        entityManager.flush();

        assertThat(q1.id()).isNotNull();
    }

    @Test
    public void findByIdAndDeletedFalse(){
        Question q1 = questions.save(Q1);

        entityManager.flush();

        final Optional<Question> findQ1 = questions.findByIdAndDeletedFalse(q1.id());

        assertThat(findQ1).isNotEmpty();
    }
}
