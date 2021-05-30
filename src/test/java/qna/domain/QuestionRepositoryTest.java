package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Question savedQuestion;
    private User savedUser;

    @BeforeEach
    void setUp() {
        //Given
        savedUser = new User("applemango", "pw", "name", "contents");
        savedUser = userRepository.save(savedUser);

        savedQuestion = new Question("Question", "Question Contents").writeBy(savedUser);
        savedQuestion = questionRepository.save(savedQuestion);
    }

    @DisplayName("저장 후에 조회해서 가져온 객체가 동일한 객체인지 확인한다")
    @Test
    void check_same_instance() {
        //Then
        Question actual = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())
                .orElseThrow(() -> new IllegalArgumentException());
        assertThat(actual).isSameAs(savedQuestion);
    }

    @DisplayName("save()실행 시, id/createdAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_save_result() {
        //Then
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getCreatedAt()).isNotNull();
    }

    @DisplayName("update()실행 시, updatedAt에 데이터가 들어가는지 확인한다")
    @Test
    void check_updatedAt() {
        //Then
        savedQuestion.setContents("Updated Question Content");
        assertThat(savedQuestion.getUpdatedAt()).isNotNull();
    }

    @DisplayName("id를 통해 삭제되지 않은 answer 객체를 조회하는지 확인한다")
    @Test
    void check_findByIdAndDeletedFalse() {
        //Then
        Question actual = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(actual.getId()).isEqualTo(savedQuestion.getId());
        assertThat(actual.isDeleted()).isFalse();
    }

    @DisplayName("삭제 표시된 answer 객체는 조회되지 않음을 확인한다")
    @Test
    void check_findByIdAndDeletedFalse_when_deleted() {
        //Given
        Question anotherQuestion = new Question("JPA Question", "Question regarding JPA").writeBy(savedUser);
        anotherQuestion = questionRepository.save(anotherQuestion);

        //When
        anotherQuestion.setDeleted(true);

        //Then
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(anotherQuestion.getId());
        assertThat(actual).isEqualTo(Optional.empty());
    }

    @DisplayName("삭제되지 않은 모든 Question 객체를 가져온다")
    @Test
    void check_findByDeletedFalse() {
        //Given
        Question anotherQuestion = new Question("JPA Question", "Question regarding JPA").writeBy(savedUser);
        anotherQuestion = questionRepository.save(anotherQuestion);

        //When
        List<Question> questionList = questionRepository.findByDeletedFalse();

        //Then
        assertThat(questionList).hasSize(2)
                .containsExactly(savedQuestion, anotherQuestion);
    }
}
