package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    private Answer savedAnswer;
    private Question savedQuestion;
    private User savedUser;

    @BeforeEach
    void setUp() {
        //Given
        savedUser = new User("applemango", "pw", "name", "contents");

        savedQuestion = new Question("JPA Hands On", "JPA Contents");
        savedQuestion = questionRepository.save(savedQuestion);

        savedAnswer = new Answer(savedUser, savedQuestion, "AnswerContents");
        savedAnswer = answerRepository.save(savedAnswer);
    }

    @DisplayName("저장 후에 조회해서 가져온 객체가 동일한 객체인지 확인한다")
    @Test
    void check_same_instance() {
        //Then
        Answer answer = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(answer).isSameAs(savedAnswer);
    }

    @DisplayName("save()실행 시, id/createdAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_save_result() {
        //Then
        assertThat(savedAnswer.getId()).isNotNull();
        assertThat(savedAnswer.getCreatedAt()).isNotNull();
    }

    @DisplayName("update()실행 시, updatedAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_updatedAt() {
        //Then
        savedAnswer.setContents("Updated Answer Content");

        assertThat(savedAnswer.getUpdatedAt()).isNotNull();
    }

    @DisplayName("id를 통해 삭제되지 않은 answer 객체를 조회하는지 확인한다")
    @Test
    void check_findByIdAndDeletedFalse() {
        //Then
        Answer actual = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(actual.getId()).isEqualTo(savedAnswer.getId());
        assertThat(actual.isDeleted()).isFalse();
    }

    @DisplayName("question_id를 통해 삭제되지 않은 answer 객체들을 조회하는지 확인한다")
    @Test
    void check_findByQuestionIdAndDeletedFalse() {
        //Given
        Answer anotherAnswer = new Answer(savedUser, savedQuestion, "AnswerContents");
        anotherAnswer = answerRepository.save(anotherAnswer);

        //When
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(savedQuestion.getId());

        //Then
        assertThat(actualList).hasSize(2)
                .containsExactly(savedAnswer, anotherAnswer);
    }
}
