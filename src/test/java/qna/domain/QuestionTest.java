package qna.domain;

import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuestionTest {

    @Test
    void 질문한_유저와_로그인_유저가_다르면_예외() {
        final User questionUser = new User("donghee.han", "1234", "donghee", "donghee.han@slipp.net");
        final User loginUser = new User("nextstep", "1234", "nextstep", "nextstep@slipp.net");
        final Question question = new Question("제목", "내용").writeBy(questionUser);

        assertThatThrownBy(() -> {
            question.delete(loginUser, null);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
