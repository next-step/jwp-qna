package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Question 정보 id로 조회 테스트")
    @Test
    void findById() {
        Optional<Question> optionalQuestion = questionRepository.findById(1L);
        assertThat(optionalQuestion.isPresent()).isTrue();

        Question question = optionalQuestion.get();
        assertAll(
                () -> assertThat(question.getId()).isEqualTo(1L),
                () -> assertThat(question.getTitle()).isEqualTo("집"),
                () -> assertThat(question.getContents()).isEqualTo("너의 집은 어디인가"),
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(question.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("Question 정보 저장 테스트")
    @Test
    void save() {
        Question question = questionRepository.save(new Question("세탁", "청소"));
        questionRepository.flush();

        Question savedQuestion = questionRepository.findById(question.getId()).get();
        assertAll(
                () -> assertThat(savedQuestion.getTitle()).isEqualTo("세탁"),
                () -> assertThat(savedQuestion.getContents()).isEqualTo("청소"),
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("Question 정보 작성자 정보 업데이트 테스트")
    @Test
    void writeBy() {
        Question question = questionRepository.findById(1L).get();
        question.writeBy(UserTest.JAVAJIGI);
        questionRepository.flush();

        Question updatedQuestion = questionRepository.findById(1L).get();
        assertThat(updatedQuestion.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId());
    }

    @DisplayName("삭제되지 않은 Question 정보 목록 조회 테스트")
    @Test
    void findByDeletedFalse() {
        List<Question> list = questionRepository.findByDeletedFalse();
        assertThat(list).hasSize(2);
    }

    @DisplayName("삭제되지 않은 Question 정보를 id 및 deleted 로 조회시 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question question = questionRepository.findByIdAndDeletedFalse(3L).get();
        assertAll(
                () -> assertThat(question.getId()).isEqualTo(3L),
                () -> assertThat(question.getTitle()).isEqualTo("차")
        );
    }

    @DisplayName("삭제된 Question 정보를 id 및 deleted 로 조회시 테스트")
    @Test
    void findByIdAndDeletedFalseDeleted() {
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(1L);
        assertThat(optionalQuestion.isPresent()).isFalse();
    }
}