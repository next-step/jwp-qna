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
        final Question question = questions.save(Q1);
        assertThat(question.getId()).isNotNull();
        assertThat(question.getTitle()).isEqualTo(Q1.getTitle());
    }

    @Test
    void findByDeletedFalse_Empty() {
        List<Question> questions = this.questions.findByDeletedFalse();
        assertThat(questions).isEmpty();
    }

    @Test
    void findByDeletedFalse() {
        questions.save(Q1);
        questions.save(Q2);

        List<Question> questions = this.questions.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
    }
}
