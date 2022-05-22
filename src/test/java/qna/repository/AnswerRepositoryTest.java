package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.AnswerRepository;
import qna.domain.QuestionTest;
import qna.domain.User;
import qna.domain.UserRepository;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    private User user;
    private Answer answer;

    @BeforeEach
    void setup() {
        user = users.save(new User("sykim", "password", "sykim", "sykim@sykim.com"));
        answer = answers.save(new Answer(user, QuestionTest.Q1, "answer1"));
    }

    @Test
    @DisplayName("없는 id 조회")
    void findNotExistAnswer() {
        assertThat(answers.findById(9999L)).isNotPresent();
    }

    @Test
    @DisplayName("저장")
    void create() {
        Optional<Answer> actual = answers.findById(answer.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("갱신")
    void update() {
        final User newUser = user = users.save(new User("gdhong", "password", "gdhong", "gdhong@gdhong.com"));
        answer.setWriter(newUser);
        Optional<Answer> actual = answers.findById(answer.getId());
        assertThat(actual).isPresent();
        assertThat(actual).contains(answer);
    }

    @Test
    @DisplayName("삭제 후 조회")
    void delete() {
        answers.deleteById(answer.getId());
        assertThat(answers.findById(answer.getId())).isNotPresent();
    }
}
