package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AnswerRepository answerRepository;

    private Question question = QuestionTest.Q1;
    private Answer answer_1 = AnswerTest.A1;
    private Answer answer_2 = AnswerTest.A2;
    private Answer saved;

    @BeforeEach
    void setUp() {
        saved = answerRepository.save(answer_1);
    }

    @AfterEach
    void cleanUp() {
        answer_1.setDeleted(false);
        answer_2.setDeleted(false);
        answerRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE answer ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("answer 저장 테스트")
    void save() {
        assertThat(saved).isEqualTo(answer_1);
    }

    @Test
    @DisplayName("answer 수정 테스트")
    void update() {
        String changedContents = "내용 바꿔보기";
        saved.setContents(changedContents);

        Optional<Answer> updatedAnswer = answerRepository.findById(saved.getId());

        assertThat(updatedAnswer.get().getContents()).isEqualTo(changedContents);
    }

    @Test
    @DisplayName("answer 제거 테스트")
    void delete() {
        answerRepository.delete(saved);

        List<Answer> answers = answerRepository.findAll();

        assertThat(answers).isEmpty();
    }

    @Test
    @DisplayName("questionId로 매칭되는 질문에 대한 답변들 정상 조회하는지 테스트")
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.saveAll(Arrays.asList(answer_1, answer_2));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertAll(
                () -> assertThat(answers).hasSize(2),
                () -> assertThat(answers).contains(answer_1, answer_2)
        );
    }

    @Test
    @DisplayName("questionId로 매칭되는 answer값 삭제되었을 시 조회 불가 테스트")
    void findByQuestionIdAndDeletedFalse_failCase() {
        answer_2.setDeleted(true);

        answerRepository.saveAll(Arrays.asList(answer_1, answer_2));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertAll(
                () -> assertThat(answers).contains(answer_1),
                () -> assertThat(answers).doesNotContain(answer_2)
        );
    }

    @Test
    @DisplayName("answerId로 매칭되는 answer값 정상 조회하는지 테스트")
    public void findByIdAndDeletedFalse() {
        Optional<Answer> findedAnswer = answerRepository.findByIdAndDeletedFalse(answer_1.getId());

        assertAll(
                () -> assertThat(findedAnswer.isPresent()).isTrue(),
                () -> assertThat(findedAnswer.get()).isEqualTo(answer_1)
        );
    }

    @Test
    @DisplayName("answerId로 매칭되는 answer값 삭제시 조회 불가 테스트")
    public void findByIdAndDeletedFalse_failCase() {
        answer_1.setDeleted(true);
        Optional<Answer> findedAnswer = answerRepository.findByIdAndDeletedFalse(answer_1.getId());

        assertThat(findedAnswer.isPresent()).isFalse();
    }
}
