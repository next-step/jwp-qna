package qna.domain.answer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.domain.user.User;
import qna.domain.user.UserTest;
import qna.domain.question.QuestionTest;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Answers Contents2"));

    @Test
    void Owner가_아닌_사람의_질문을_Answer는_예외를_출력() {
        User notOwnerUser = UserTest.SANJIGI;
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));

        assertThat(answer.deleted()).isFalse();
        assertThatThrownBy(() -> answer.deletedBy(notOwnerUser))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void Owner가_answer을_삭제하면_mark_가_표시된다() throws Exception {
        User sameOwner = UserTest.SANJIGI;
        Answer answer = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));

        assertThat(answer.deleted()).isFalse();
        answer.deletedBy(sameOwner);
        assertThat(answer.deleted()).isTrue();
    }
}
