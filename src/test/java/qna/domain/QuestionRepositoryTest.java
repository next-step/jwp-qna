package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("삭제되지 않은 question 리스트를 반환한다.")
    void find_not_deleted_question() {
        questionRepository.saveAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2));
        List<Question> questions = questionRepository.findByDeletedFalse();
        for (Question question : questions) {
            assertThat(question.isDeleted()).isFalse();
        }
    }


    @Test
    @DisplayName("questionId를 이용하여 삭제되지 않은 question을 반환한다.")
    void find_not_deleted_question_with_question_id() {
        Question requestedQuestion = questionRepository.save(QuestionTest.Q1);
        Question foundQuestion = questionRepository
            .findByIdAndDeletedFalse(requestedQuestion.getId())
            .orElseThrow(NotFoundException::new);
        assertThat(requestedQuestion.equals(foundQuestion)).isTrue();

    }
}