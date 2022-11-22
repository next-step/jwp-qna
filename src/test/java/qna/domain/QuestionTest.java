package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.constant.DeleteErrorMessage;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private Question question;
    private User writer;

    private Answer answer;

    @BeforeEach
    void setUp() {
        writer = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question("title1", "contents1").writeBy(writer);
        answer = new Answer(writer, question, "Answers Contents1");
    }

    @Test
    @DisplayName("로그인 사용자와 질문자가 다르면 삭제 불가능")
    void validateDelete1() {
        assertThatThrownBy(() -> question.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(DeleteErrorMessage.NOT_HAVE_PERMISSION_DELETE_QUESTION);
    }

    @Test
    @DisplayName("답변이 있으면 삭제 불가능")
    void validateDelete2() {
        question.addAnswer(AnswerTest.A2);

        assertThatThrownBy(() -> question.delete(writer))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage(DeleteErrorMessage.NOT_EMPTY_ANSWER_DELETE_QUESTION);
    }

    @Test
    @DisplayName("질문자와 답변글의 답변자가 모두 같을 경우 삭제 가능")
    void validateDelete3() throws CannotDeleteException {
        question.addAnswer(answer);

        question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 성공 후 히스토리 등록")
    void deleteSuccessAndSaveDeleteHistory() throws CannotDeleteException {
        DeleteHistories deleteHistorys = question.delete(writer);

        assertThat(question.isDeleted()).isTrue();
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), writer, LocalDateTime.now());
        assertThat(deleteHistorys.deleteHistories.contains(deleteHistory)).isTrue();

    }
}
