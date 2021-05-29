package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    private Answer expected;
    private Question question;

    @BeforeEach
    void setUp() {
        //Given
        question = questionRepository.save(QuestionTest.Q1);

        expected = answerRepository.save(AnswerTest.A1);
    }

    @DisplayName("저장 후에 조회해서 가져온 객체가 동일한 객체인지 확인한다")
    @Test
    void check_same_instance() {
        //Then
        Answer answer = answerRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(answer).isSameAs(expected);
    }

    @DisplayName("save()실행 시, id/createdAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_save_result() {
        //Then
        assertThat(expected.getId()).isNotNull();
        assertThat(expected.getCreatedAt()).isNotNull()
                .isBefore(LocalDateTime.now());
    }

    @DisplayName("update()실행 시, updatedAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_updatedAt() {
        //Then
        expected.setContents("Updated Answer Content");

        assertThat(expected.getUpdatedAt()).isNotNull()
                .isAfter(expected.getCreatedAt())
                .isBefore(LocalDateTime.now());

    }

    @DisplayName("id를 통해 삭제되지 않은 answer 객체를 조회하는지 확인한다")
    @Test
    void check_findByIdAndDeletedFalse() {
        //Then
        Answer actual = answerRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.isDeleted()).isFalse();
    }

    @DisplayName("question_id를 통해 삭제되지 않은 answer 객체들을 조회하는지 확인한다")
    @Test
    void check_findByQuestionIdAndDeletedFalse() {
        //When
        Question q2 = questionRepository.save(QuestionTest.Q2);

        /*  expected와 answerOfQ1 객체의 questionId는 모두 QuestionTest.Q1 */
        Answer answerOfQ1 = answerRepository.save(AnswerTest.A1);
        Answer answerOfQ2 = answerRepository.save(new Answer(UserTest.JAVAJIGI, q2, "Answer Content"));

        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(AnswerTest.A1.getQuestionId());

        //Then
        assertThat(actualList).hasSize(2)
                .containsExactly(expected, answerOfQ1)
                .doesNotContain(answerOfQ2);
    }
}
