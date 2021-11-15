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
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("save 테스트")
    @Test
    void save() {
        // when
        Answer newAnswer = answerRepository.save(A1);

        // then
        assertAll(
                () -> assertThat(newAnswer.getId()).isNotNull()
                , () -> assertThat(newAnswer.getWriterId()).isEqualTo(A1.getWriterId())
                , () -> assertThat(newAnswer.getQuestionId()).isEqualTo(A1.getQuestionId())
                , () -> assertThat(newAnswer.getContents()).isEqualTo(A1.getContents())
        );
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        // when
        Answer newAnswer = answerRepository.save(A1);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(newAnswer.getId());

        // then
        assertAll(
                () -> assertThat(optionalAnswer.get()).isNotNull()
                , () -> assertThat(optionalAnswer.get()).isEqualTo(newAnswer)
        );
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        // given
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");

        // when
        Answer newAnswer1 = answerRepository.save(answer1);
        Answer newAnswer2 = answerRepository.save(answer2);
        List<Answer> newAnswers = answerRepository.findByQuestionIdAndDeletedFalse(newAnswer1.getQuestionId());

        // then
        assertAll(
                () -> assertThat(newAnswers).hasSize(1)
                , () -> assertThat(newAnswers).containsExactly(newAnswer1)
        );
    }
}
