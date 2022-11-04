package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("answer 저장 확인")
    void save_answer() {
        //given, when
        Answer answer = answerRepository.save(A1);

        //then
        assertAll(
            () -> assertThat(answer.getId()).isEqualTo(A1.getId()),
            () -> assertThat(answer.getQuestionId()).isEqualTo(A1.getQuestionId())
        );
    }

    @Test
    @DisplayName("answer 조회 확인")
    void read_answer() {
        //given
        Answer answer = answerRepository.save(A2);

        //when
        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(A2.getId());

        // then
        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(answer)
        );
    }

    @Test
    @DisplayName("answer 수정 확인")
    void update_answer() {
        //given
        Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Test Content 1"));

        //when
        answer.setQuestionId(10L);
        Answer result = answerRepository.save(answer);

        // then
        assertAll(
            () -> assertThat(result.getQuestionId()).isEqualTo(10L)
        );
    }

    @Test
    @DisplayName("answer 삭제 확인")
    void delete_repository() {
        // given
        Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Test Content 1"));

        // when
        answerRepository.delete(answer);
        Optional<Answer> expectAnswer = answerRepository.findById(UserTest.SANJIGI.getId());

        // then
        assertThat(expectAnswer.isPresent()).isFalse();
    }
}
