package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private AnswerRepository answerRepository;

    private Answer answer_1 = AnswerTest.A1;
    private Answer saved;

    @BeforeEach
    void setUp() {
        saved = answerRepository.save(answer_1);
    }

    @AfterEach
    void cleanUp() {
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
        saved.setContents("내용 바꿔보기");

        Optional<Answer> updatedAnswer = answerRepository.findById(saved.getId());

        assertThat(updatedAnswer.get().getContents()).isEqualTo("내용 바꿔보기");
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
        Question question = QuestionTest.Q1;
        Answer answer1 = AnswerTest.A1;
        Answer answer2 = AnswerTest.A2;
        answer1.toQuestion(question);
        answer2.toQuestion(question);
        answerRepository.saveAll(Arrays.asList(answer1, answer2));

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertAll(
                () -> assertThat(answers).hasSize(2),
                () -> assertThat(answers).contains(answer1, answer2)
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
}
