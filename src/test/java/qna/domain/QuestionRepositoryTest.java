package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Question 정보 id로 조회 테스트")
    @Test
    void findById() {
        Optional<Question> optionalQuestion = questionRepository.findById(2001L);
        assertThat(optionalQuestion).isPresent();

        Question question = optionalQuestion.get();
        assertAll(
                () -> assertThat(question.getId()).isEqualTo(2001L),
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

        Optional<Question> optionalQuestion = questionRepository.findById(question.getId());
        assertThat(optionalQuestion).isPresent();

        Question savedQuestion = optionalQuestion.get();
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
        Question question = questionRepository.findById(2001L).get();
        assertThat(question.getWriter()).isNull();

        User writer = userRepository.findById(1001L).get();
        question.writeBy(writer);
        questionRepository.flush();

        Question updatedQuestion = questionRepository.findById(2001L).get();
        assertThat(updatedQuestion.getWriter()).isEqualTo(writer);
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
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(2003L);
        assertThat(optionalQuestion).isPresent();

        Question question = optionalQuestion.get();
        assertAll(
                () -> assertThat(question.getId()).isEqualTo(2003L),
                () -> assertThat(question.getTitle()).isEqualTo("차")
        );
    }

    @DisplayName("삭제된 Question 정보를 id 및 deleted 로 조회시 테스트")
    @Test
    void findByIdAndDeletedFalseDeleted() {
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedFalse(2001L);
        assertThat(optionalQuestion).isNotPresent();
    }
}
