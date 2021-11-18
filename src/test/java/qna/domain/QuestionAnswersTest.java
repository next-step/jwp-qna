package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class QuestionAnswersTest {

    private Answer answer;
    private QuestionAnswers questionAnswers;

    @BeforeEach
    void setUp() {
        answer = Answer.of(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        questionAnswers = new QuestionAnswers(Arrays.asList(answer));
    }

    @DisplayName("delete 성공 테스트")
    @Test
    void delete_success() throws CannotDeleteException {
        // when
        questionAnswers.delete(UserTest.JAVAJIGI);

        // then
        assertThat(questionAnswers.getAnswers().get(0).isDeleted()).isTrue();
    }

    @DisplayName("delete 실패 테스트")
    @Test
    void delete_failure() {
        // when & then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> questionAnswers.delete(UserTest.SANJIGI));
    }
}
