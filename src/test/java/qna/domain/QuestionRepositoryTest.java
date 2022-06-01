package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questions;
    @Autowired
    UserRepository users;

    private User user;
    private Question question;

    @BeforeEach
    void setUp() {
        user = users.save(new User("lcjltj","password", "chanjun", "lcjltj@gmail.com"));
        question = questions.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    void findByIdAndDeletedFalse() throws CannotDeleteException {
        // given
        question.delete(user);
        // when
        Optional<Question> expected = questions.findByIdAndDeletedFalse(question.getId());
        // then
        assertThatThrownBy(() -> expected.get())
                .isInstanceOf(NoSuchElementException.class);

    }

    @Test
    void findByDeletedFalse() throws CannotDeleteException {
        // given
        question.delete(user);
        // when
        List<Question> questions = this.questions.findByDeletedFalse();
        // then
        assertThat(questions).hasSize(0);
    }
}