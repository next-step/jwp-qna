package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("Question 생성")
    @Test
    void teat_save() {
        //given & when
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);
        questionRepository.flush();
        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());
        //then
        assertAll(
                () -> assertThat(findQuestion.isPresent()).isTrue(),
                () -> assertThat(savedQuestion.equals(findQuestion.get())).isTrue()
        );
    }

    @DisplayName("삭제되지 않은 Question 목록 조회")
    @Test
    void teat_findByDeletedFalse() {
        //given
        QuestionTest.Q1.setDeleted(true);
        questionRepository.save(QuestionTest.Q1);
        Question savedQuestion = questionRepository.save(QuestionTest.Q2);
        questionRepository.flush();
        //when
        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        //then
        assertAll(
                () -> assertThat(findQuestions).hasSize(1),
                () -> assertThat(findQuestions).contains(savedQuestion)
        );
    }

    @DisplayName("Id로 삭제되지 않은 Question 목록 조회")
    @Test
    void teat_findByIdAndDeletedFalse() {
        //given
        QuestionTest.Q1.setDeleted(true);
        Question deletedQuestion = questionRepository.save(QuestionTest.Q1);
        Question savedQuestion = questionRepository.save(QuestionTest.Q2);
        questionRepository.flush();
        //when
        Optional<Question> findDeletedQuestions = questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId());
        Optional<Question> findSavedQuestions = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
        //then
        assertAll(
                () -> assertThat(findDeletedQuestions.isPresent()).isFalse(),
                () -> assertThat(findSavedQuestions.isPresent()).isTrue()
        );
    }
}