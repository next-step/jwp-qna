package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class AnswersTest {

    @Test
    @DisplayName("답변삭제 유효한 유저인 경우 오류가 발생 안하는지 체크")
    void checkDeleteAnswers_validate_test() {
        final Answers answers = new Answers(Arrays.asList(AnswerTest.A1));
        assertThatCode(() -> answers.checkDeleteAuth(UserTest.JAVAJIGI))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("답변삭제 유효하지 않은 유저인 경우 오류가 발생 하는지 체크")
    void checkDeleteAnswers_invalidate_test() {
        //one user answer delete test
        final Answers answers = new Answers(Arrays.asList(AnswerTest.A1));
        assertThatThrownBy(() -> answers.checkDeleteAuth(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        //two user answer delete test
        Answers answers2 = new Answers(Arrays.asList(AnswerTest.A1, AnswerTest.A2));
        assertThatThrownBy(() -> answers2.checkDeleteAuth(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("저장할 삭제목록 생성 테스트")
    void deleteAndGetDeleteHistories_test() {
        final Answer answer1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
        final Answer answer2 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents2");
        final Answers answers = new Answers(Arrays.asList(answer1, answer2));
        List<DeleteHistory> deleteHistories = answers.deleteAndGetDeleteHistories();
        //list contain test
        assertThat(deleteHistories
                .contains(new DeleteHistory(ContentType.ANSWER, answer1.getId(),
                        answer1.getWriter(), LocalDateTime.now())))
                .isTrue();
        //list size test
        assertThat(deleteHistories.size()).isEqualTo(2);
        //delete flag test
        assertThat(answer1.isDeleted()).isTrue();
        assertThat(answer2.isDeleted()).isTrue();
    }
}
