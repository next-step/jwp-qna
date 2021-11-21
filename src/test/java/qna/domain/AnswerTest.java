package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {

    @DisplayName("답변 삭제 할때 삭제 상태로 변경해준다.")
    @Test
    void deleteTest() throws CannotDeleteException {
        final User user = new User(1L,"lsm","password","이승민","test@test.com");
        final Question question = new Question(1L,"Question","Question Contents1").writeBy(user);
        final Answer answer = new Answer(1L,user, question, "Answers Contents1");

        answer.delete(user);

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변 삭제 시 DeleteHistory에 이력정보객체 생성.")
    @Test
    void deleteHistoryToAnswer() throws CannotDeleteException {
        final User user = new User(1L,"lsm","password","이승민","test@test.com");
        final Question question = new Question(1L,"Question","Question Contents1").writeBy(user);
        final Answer answer = new Answer(1L,user, question, "Answers Contents1");

        final DeleteHistory deleteHistory = answer.delete(user);

        assertThat(deleteHistory).isNotNull();
        assertThat(deleteHistory).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), user, LocalDateTime.now()));
    }

    @DisplayName("답변 등록자가 삭제하려는 사람과 다를때 에러")
    @Test
    void deleteAnswerByUserError() throws CannotDeleteException {
        final User user = new User(1L,"lsm","password","이승민","test@test.com");
        final Question question = new Question(1L,"Question","Question Contents1").writeBy(user);
        final Answer answer = new Answer(1L,user, question, "Answers Contents1");

        assertThatThrownBy(() -> {
            answer.delete(new User(2L,"lsm2", "password", "이승민", "test@test.com"));
        }).isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
