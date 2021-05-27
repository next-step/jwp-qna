package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("DB에 저장된 Entity와 저장하기전 Entity가 동일한지 확인")
    @Test
    void insertTest() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "AnswerRepository 테스트입니다.");

        Answer savedAnswer = answerRepository.save(answer);

        assertThat(savedAnswer).isEqualTo(answer);
    }
    
    @DisplayName("삭제상태가 아닌 답변을 질문ID로 조회 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        Answer answer1 = new Answer(UserTest.JAVAJIGI, savedQuestion, "답변1");
        answerRepository.save(answer1);
        Answer answer2 = new Answer(UserTest.JAVAJIGI, savedQuestion, "답변2");
        answerRepository.save(answer2);

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId());

        assertThat(findAnswers).hasSize(2);
        assertThat(findAnswers).containsExactly(answer1, answer2);
    }

    @DisplayName("존재하지않는 질문ID로 조회 테스트")
    @Test
    void findByInvalidQuestionIdAndDeletedFalse() {
        Long questionId = 1L;

        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(questionId);

        assertThat(findAnswers).isEmpty();;
        assertThat(findAnswers).hasSize(0);
    }

    @DisplayName("삭제상태가 아닌 답변을 답변ID로 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        Answer answer = new Answer(UserTest.JAVAJIGI, savedQuestion, "답변");
        answerRepository.save(answer);

        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(findAnswer).contains(answer);
        assertTrue(findAnswer.isPresent());
    }

    @DisplayName("존재하지 않는 답변ID로 조회 테스트")
    @Test
    void findByInvalidIdAndDeletedFalse() {
        Long answerId = 1L;
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(answerId);
        assertFalse(findAnswer.isPresent());
    }

}
