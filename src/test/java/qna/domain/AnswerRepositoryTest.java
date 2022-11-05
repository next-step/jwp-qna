package qna.domain;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public Answer answerTest;

    @BeforeEach
    void setUp() {
        answerTest = AnswerTest.A1;
    }

    @Test
    @DisplayName("Answer 저장한 엔티티의 id로 조회한 경우 동일성 테스트")
    void find() {
        Answer saveAnswer = answerRepository.save(answerTest);
        Answer findAnswer = answerRepository.findById(saveAnswer.getId()).orElse(null);
        assertAll(
                () -> assertEquals(saveAnswer, findAnswer),
                () -> assertEquals(saveAnswer.getQuestion(), answerTest.getQuestion())
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 검증")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        Question question = questionRepository.save(QuestionTest.Q1);
        Answer answer = answerRepository.save(AnswerTest.A1);
        List<Answer> answers1 = answerRepository.findByQuestionAndDeletedFalse(question);
        answer.setDeleted(true);
        List<Answer> answers2 = answerRepository.findByQuestionAndDeletedFalse(question);

        assertAll(
                () -> assertThat(answers1).hasSize(1),
                () -> assertThat(answers2).isEmpty()
        );
    }



}
