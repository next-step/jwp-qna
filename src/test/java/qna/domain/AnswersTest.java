package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class AnswersTest {

    @Test
    @DisplayName("삭제 테스트")
    void delete() throws CannotDeleteException {
        // given
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        Answer answer2 = new Answer(2L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents2");
        Answers answers = new Answers(Arrays.asList(answer1, answer2));
        // when
        answers.deleteAllByOwner(UserTest.JAVAJIGI);
        // then
        answers.getAnswers().forEach(answer -> {
            assertThat(answer.isDeleted()).isTrue();
        });
    }

    @Test
    @DisplayName("질문자와 답변 글의 모든 답변자 같은 경우 삭제 테스트")
    void delete_exception() {
        // given
        Answer answer1 = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents1");
        Answer answer2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q1, "contents2");
        Answers answers = new Answers(Arrays.asList(answer1, answer2));
        // when & then
        assertThatThrownBy(() -> answers.deleteAllByOwner(UserTest.JAVAJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
