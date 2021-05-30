package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    EntityManager entityManager;

    @DisplayName("저장하기")
    @Test
    void save() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");

        Answer savedAnswer = answerRepository.save(answer);

        Answer findAnswer = answerRepository.findById(savedAnswer.getId())
                .orElseThrow(() -> new IllegalStateException());

        assertThat(savedAnswer).isEqualTo(findAnswer);
        assertThat(savedAnswer).isSameAs(findAnswer);
    }

    @DisplayName("수정하기")
    @Test
    void update() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");
        Answer savedAnswer = answerRepository.save(answer);

        savedAnswer.setContents("testUpdateContent");

        Answer findAnswer = answerRepository.findById(answer.getId()).orElseThrow(() -> new IllegalStateException());
        assertThat(findAnswer.getContents()).isEqualTo("testUpdateContent");
    }

    @DisplayName("삭제하기")
    @Test
    void delete() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        Question question = new Question("testTitle", "testContent");
        Answer answer = new Answer(writer, question, "save test");
        Answer savedAnswer = answerRepository.save(answer);

        answerRepository.delete(savedAnswer);

        assertThat(answerRepository.findById(savedAnswer.getId())).isEqualTo(Optional.empty());
    }

    @DisplayName("지연로딩")
    @Test
    void lazy() {
        User writer = new User("testUserId", "testPassword", "testName", "test@email.com");
        userRepository.save(writer);
        Question question = new Question("testTitle", "testContent");
        questionRepository.save(question);
        Answer answer = new Answer(writer, question, "save test");
        Answer saveAnswer = answerRepository.save(answer);
        entityManager.flush();
        entityManager.clear();

        Answer findAnswer = answerRepository.findById(saveAnswer.getId()).orElseThrow(() -> new IllegalStateException());

        assertThat(entityManagerFactory.getPersistenceUnitUtil().isLoaded(findAnswer.getQuestion())).isFalse();
        assertThat(entityManagerFactory.getPersistenceUnitUtil().isLoaded(findAnswer.getWriter())).isFalse();

        String name = findAnswer.getWriter().getName();
        assertThat(name).isEqualTo("testName");
    }
}
