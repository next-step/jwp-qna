package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static qna.domain.AnswerTest.*;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
@DisplayName("AnswerRepository 테스트")
class AnswerRepositoryTest {


    @Autowired
    private AnswerRepository answers;

    @Autowired
    private QuestionRepository questions;

    private Answer answer1;
    private Answer answer2;

    private Answer deletedAnswer1;

    private Question question;

    @BeforeEach
    void setUp() {
        question = questions.save(Q1);

        answer1 = answers.save(A1);
        answer2 = answers.save(A2);
        deletedAnswer1 = answers.save(DELETED_ANSWER1);
    }

    @Test
    @DisplayName("findById_정상_저장_전_후_동일한_객체_조회")
    void findById_정상_저장_전_후_동일한_객체_조회() {
        // Given
        Answer expectedResult = answer1;

        // When
        Optional<Answer> actualResult = answers.findById(expectedResult.getId());

        // Then
        assertThat(actualResult).containsSame(expectedResult);
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse_정상_삭제_되지_않은_데이터만_조회")
    void findByQuestionIdAndDeletedFalse_정상_삭제_되지_않은_데이터만_조회() {
        // Given
        final int expectedResult = 2;

        // When
        List<Answer> foundQuestion = answers.findByQuestionIdAndDeletedFalse(question.getId());

        // Then
        assertThat(foundQuestion.size()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse_정상_데이터_있음")
    void findByIdAndDeletedFalse_정상_데이터_있음() {
        // Given
        Answer nonDeletedAnswer = answer1;

        // When
        Optional<Answer> foundAnswer = answers.findByIdAndDeletedFalse(nonDeletedAnswer.getId());

        // Then
        assertThat(foundAnswer).isPresent();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse_오류_데이터_없음")
    void findByIdAndDeletedFalse_오류_데이터_없음() {
        // Given
        Answer deletedAnswer = deletedAnswer1;

        // When
        Optional<Answer> foundAnswer = answers.findByIdAndDeletedFalse(deletedAnswer.getId());

        // Then
        assertThat(foundAnswer).isEmpty();
    }
}
