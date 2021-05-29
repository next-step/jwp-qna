package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    private Question expected;

    @BeforeEach
    void setUp() {
        //Given
        expected = questionRepository.save(QuestionTest.Q1);
    }

    @DisplayName("저장 후에 조회해서 가져온 객체가 동일한 객체인지 확인한다")
    @Test
    void check_same_instance() {
        //Then
        Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(actual).isSameAs(expected);
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
        expected.setContents("Updated Question Content");

        assertThat(expected.getUpdatedAt()).isNotNull()
                .isAfter(expected.getCreatedAt())
                .isBefore(LocalDateTime.now());
    }

    @DisplayName("id를 통해 삭제되지 않은 answer 객체를 조회하는지 확인한다")
    @Test
    void check_findByIdAndDeletedFalse() {
        //Then
        Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.isDeleted()).isFalse();
    }

    @DisplayName("삭제 표시된 answer 객체는 조회되지 않음을 확인한다")
    @Test
    void check_findByIdAndDeletedFalse_when_deleted() {
        //Given
        Question deletedQuestion = questionRepository.save(QuestionTest.Q2);

        //When
        deletedQuestion.setDeleted(true);

        //Then
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId());
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @DisplayName("삭제되지 않은 모든 Question 객체를 가져온다")
    @Test
    void check_findByDeletedFalse() {
        //Given
        QuestionTest.Q2.setDeleted(false); //혹시나 위 테스트가 먼저 수행되어 deleted = true가 되었을 경우 대비
        Question q2 = questionRepository.save(QuestionTest.Q2);

        //When
        List<Question> questionList = questionRepository.findByDeletedFalse();

        //Then
        assertThat(questionList).hasSize(2)
                .containsExactly(expected, q2);
    }
}
