package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private User user;
    private Question question;

    @BeforeEach
    void setup() {
        user = users.save(new User("sykim", "password", "sykim", "sykim@sykim.com"));
        question = questions.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        assertThat(questions.findById(9999L)).isNotPresent();
    }

    @Test
    @DisplayName("저장")
    void create() {
        Optional<Question> actual = questions.findById(question.getId());
        assertThat(actual).isPresent();
    }


    @Test
    @DisplayName("갱신")
    void update() {
        final User newUser = user = users.save(new User("gdhong", "password", "gdhong", "gdhong@gdhong.com"));
        question.setWriter(newUser);
        Optional<Question> actual = questions.findById(question.getId());
        assertThat(actual).isPresent();
        assertThat(actual).contains(question);
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        questions.deleteById(question.getId());
        assertThat(questions.findById(question.getId())).isNotPresent();
    }
}
