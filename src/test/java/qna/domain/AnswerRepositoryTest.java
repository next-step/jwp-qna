package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Answer 생성")
    @Test
    void teat_save() {
        //given & when
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());
        //then
        assertAll(
                () -> assertThat(findAnswer.isPresent()).isTrue(),
                () -> assertThat(savedAnswer.equals(findAnswer.get())).isTrue()
        );
    }

    @DisplayName("Question Id로 삭제되지 않은 Answer 목록 조회")
    @Test
    void teat_findByQuestionIdAndDeletedFalse() {
        //given
        AnswerTest.A1.setDeleted(true);
        Answer deletedAnswer = answerRepository.save(AnswerTest.A1);
        Answer savedAnswer = answerRepository.save(AnswerTest.A2);
        //when
        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(deletedAnswer.getQuestionId());
        //then
        assertAll(
                () -> assertThat(findAnswers).hasSize(1),
                () -> assertThat(findAnswers).contains(savedAnswer)
        );
    }

    @DisplayName("Id로 삭제되지 않은 Answer 목록 조회")
    @Test
    void teat_findByIdAndDeletedFalse() {
        //given
        AnswerTest.A1.setDeleted(true);
        Answer deletedAnswer = answerRepository.save(AnswerTest.A1);
        Answer savedAnswer = answerRepository.save(AnswerTest.A2);
        //when
        Optional<Answer> findAnswer1 = answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId());
        Optional<Answer> findAnswer2 = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
        //then
        assertAll(
                () -> assertThat(findAnswer1.isPresent()).isFalse(),
                () -> assertThat(findAnswer2.isPresent()).isTrue()
        );
    }
}