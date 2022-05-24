package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void userSetUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("질문이 저장된다.")
    void save() {
        Question q1 = questionRepository.save(Q1);

        final Optional<Question> findQ1 = questionRepository.findById(q1.getId());

        assertAll(() -> {
            assertThat(findQ1.isPresent()).isTrue();
            assertThat(findQ1.get()).isEqualTo(q1);
        });
    }

    @Test
    @DisplayName("질문이 변경된다.")
    @Rollback
    void update() {
        Question q1 = questionRepository.save(Q1);
        q1.setContents("변경데이터");

        final Optional<Question> findQ1 = questionRepository.findById(q1.getId());

        assertAll(
                () -> assertThat(findQ1).isPresent(),
                () -> assertThat(findQ1.get().getContents()).isEqualTo("변경데이터")
        );
    }

    @Test
    @DisplayName("질문이 삭제된다")
    @Rollback
    void delete() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        questionRepository.delete(q1);

        assertThat(questionRepository.count()).isEqualTo(1);
        questionRepository.flush();
    }

    @Test
    @DisplayName("삭제가 안된 질문을 조회한다.")
    @Rollback
    void findByDeletedFalse() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        final List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(2);
    }

    @Test
    @DisplayName("삭제가 안된 질문을 ID 값으로 조회한다.")
    @Rollback
    void findByIdDeletedFalse() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);
        q1.setDeleted(true);


        Optional<Question> findQ1 = questionRepository.findByIdAndDeletedFalse(q1.getId());
        Optional<Question> findQ2 = questionRepository.findByIdAndDeletedFalse(q2.getId());

        assertAll(
            () -> assertThat(findQ1).isNotPresent(),
            () -> assertThat(findQ2).isPresent()
        );
    }

    @Test
    @DisplayName("질문 작성자인지 확인한다")
    void isOwner() {
        final User testUser = userRepository.save(
                new User("userId", "password", "name", "email"));

        final Question question = questionRepository.save(new Question("타이틀", "콘텐츠").writeBy(testUser));

        final Optional<Question> findQuestion = questionRepository.findById(question.getId());

        assertAll(
                () -> assertThat(findQuestion).isPresent(),
                () -> assertThat(findQuestion.get().isOwner(testUser)).isTrue()
        );
    }
}
