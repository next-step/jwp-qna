package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AnswerRepository는 ")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManager entityManager;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = new User("javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question("title1", "contents1");
        answer = new Answer(user, question, "contents1");

        entityManager.persist(user);
        entityManager.persist(question);
        answerRepository.save(answer);
    }

    @DisplayName("questionId로 조회할 수 있다")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(answers).hasSize(1);
    }

    @DisplayName("id로 조회할 수 있다")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.findByIdAndDeletedFalse(1L)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(answer).isEqualTo(answer);
    }

    @AfterEach
    void tearDown() {
        answerRepository.deleteAll();
    }
}
