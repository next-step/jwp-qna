package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("findByDeletedFalse 테스트")
    @Test
    void findByDeletedFalse() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);

        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questionList).hasSize(2),
                () -> {
                    Question question = questionList.get(0);
                    assertAll(
                            () -> assertThat(question.getId()).isEqualTo(savedQuestion1.getId()),
                            () -> assertThat(question.getTitle()).isEqualTo(savedQuestion1.getTitle()),
                            () -> assertThat(question.getContents()).isEqualTo(savedQuestion1.getContents()),
                            () -> assertThat(question.getWriterId()).isEqualTo(savedQuestion1.getWriterId()),
                            () -> assertThat(question.isDeleted()).isEqualTo(savedQuestion1.isDeleted())
                    );
                },
                () -> {
                    Question question = questionList.get(1);
                    assertAll(
                            () -> assertThat(question.getId()).isEqualTo(savedQuestion2.getId()),
                            () -> assertThat(question.getTitle()).isEqualTo(savedQuestion2.getTitle()),
                            () -> assertThat(question.getContents()).isEqualTo(savedQuestion2.getContents()),
                            () -> assertThat(question.getWriterId()).isEqualTo(savedQuestion2.getWriterId()),
                            () -> assertThat(question.isDeleted()).isEqualTo(savedQuestion2.isDeleted())
                    );
                }
        );
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);

        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(1L);
        Question question = questionOptional.get();

        assertAll(
                () -> assertThat(question.getId()).isEqualTo(savedQuestion1.getId()),
                () -> assertThat(question.getTitle()).isEqualTo(savedQuestion1.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(savedQuestion1.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(savedQuestion1.getWriterId()),
                () -> assertThat(question.isDeleted()).isEqualTo(savedQuestion1.isDeleted())
        );
    }

}
