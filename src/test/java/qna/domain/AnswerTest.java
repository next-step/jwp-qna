package qna.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    void 댓글_삭제() {
        Answer answer = new Answer("댓글");
        DeleteHistory deleteHistory = answer.delete();
        assertThat(deleteHistory).isNotNull();
        assertThat(answer.isDeleted()).isTrue();
    }
}
