package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @Test
    void save() {
        final User user = users.save(UserTest.JAVAJIGI);
        final Question question = questions.save(
                Q1.writeBy(user)
        );
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
        final User user1 = users.save(UserTest.JAVAJIGI);
        final User user2 = users.save(UserTest.SANJIGI);
        questions.save(Q1.writeBy(user1));
        questions.save(Q2.writeBy(user2));

        List<Question> questions = this.questions.findByDeletedFalse();
        assertThat(questions.size()).isEqualTo(2);
    }

    @Test
    void nullableFalse() {
        Question question = new Question(null, "contents");

        assertThatThrownBy(() -> questions.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
