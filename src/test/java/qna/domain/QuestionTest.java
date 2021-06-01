package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI.id());
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI.id());

    @Autowired
    private QuestionRepository questions;

    @Test
    public void save(){
        final Question q1 = questions.save(Q1);

        assertThat(q1.id()).isNotNull();
    }

    @Test
    public void findByIdAndDeletedFalse(){
        final Question q1 = questions.save(Q1);

        final Optional<Question> findQ1 = questions.findByIdAndDeletedFalse(q1.id());

        assertThat(findQ1).isNotEmpty();
    }
}
