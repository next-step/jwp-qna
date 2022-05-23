package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionTest {
    final User aUser = new User("donghee.han", "1234", "donghee", "donghee.han@slipp.net");
    final User bUser = new User("nextstep", "1234", "nextstep", "nextstep@slipp.net");
    final Question aUserQuestion = new Question("제목", "내용").writeBy(aUser);
    final Answer aUserAnswer = new Answer(aUser, aUserQuestion, "댓글");
    final Answer bUserAnswer = new Answer(bUser, aUserQuestion, "댓글");

    @Test
    void 질문한_유저와_로그인_유저가_같으면_삭제가능() throws CannotDeleteException {
        aUserQuestion.delete(aUser, new Answers());
    }

    @Test
    void 질문한_유저와_로그인_유저가_다르면_삭제불가() {
        assertThatThrownBy(() -> {
            aUserQuestion.delete(bUser, new Answers());
        }).isInstanceOf(CannotDeleteException.class).hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 질문한_유저와_댓글_유저가_다르면_삭제불가() {
        assertThatThrownBy(() -> {
            final Answers answers = new Answers(Arrays.asList(bUserAnswer));
            aUserQuestion.delete(aUser, answers);
        }).isInstanceOf(CannotDeleteException.class).hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void 질문_삭제_테스트() throws CannotDeleteException {
        final Answers answers = new Answers(Arrays.asList(aUserAnswer));

        final List<DeleteHistory> history = aUserQuestion.delete(aUser, answers);

        assertThat(aUserQuestion.isDeleted()).isTrue();
        history.contains(new DeleteHistory(ContentType.QUESTION, aUserQuestion.getId(), LocalDateTime.now()));
    }
}
