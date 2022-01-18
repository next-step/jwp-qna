package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    private static final Long QUESTION_ID = 1L;

    @Test
    @DisplayName("questionId를 이용하여 삭제되지 않은 answer 리스트를 반환한다.")
    void find_not_deleted_answer_with_question_id() {
        answerRepository.saveAll(Arrays.asList(AnswerTest.A3, AnswerTest.A4));
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(QUESTION_ID);
        for (Answer answer : answers) {
            assertThat(answer.getQuestion().equals(QUESTION_ID)).isTrue();
            assertThat(answer.isDeleted()).isFalse();
        }
    }

    @Test
    @DisplayName("answerId를 이용하여 삭제되지 않은 answer를 반환한다.")
    void find_not_deleted_answer_with_id() {
        Answer requestedAnswer = answerRepository.save(AnswerTest.A3);
        Answer foundAnswer = answerRepository.findByIdAndDeletedFalse(requestedAnswer.getId())
            .orElseThrow(NotFoundException::new);

        assertThat(requestedAnswer.equals(foundAnswer)).isTrue();
    }

}