package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    void 댓글_삭제() {
        User user = new User("donghee.han", "password", "donghee", "donghee.han@slipp.net");
        Answer answer = new Answer("댓글");
        DeleteHistory deleteHistory = answer.delete(user);
        assertThat(deleteHistory).isNotNull();
        assertThat(answer.isDeleted()).isTrue();
        assertThat(deleteHistory.getDeleteUser()).isEqualTo(user);
    }
}
