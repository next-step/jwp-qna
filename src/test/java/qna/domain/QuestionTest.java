package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

@DisplayName("Question 클래스")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    private Question question;
    private Answer answer;

    @BeforeEach
    public void setUp() throws Exception {
        question = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer = new Answer(1L, UserTest.JAVAJIGI, question, "Answers Contents1");
        question.addAnswer(answer);
    }

    @DisplayName("validateOwner 메서드는 다른 유저가 쓴 질문일 경우 CannotDeleteException이 발생한다.")
    @Test
    void validateOwner() {
        assertThatThrownBy(
                () -> question.validateOwner(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("validateAnswersOwner 메서드는 다른 유저가 쓴 답변일 경우 CannotDeleteException이 발생한다.")
    @Test
    void validateAnswersOwner() {
        assertThatThrownBy(
                () -> question.validateAnswersOwner(UserTest.SANJIGI)
        ).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("delete 메서드는 질문과 답변에 대한 삭제 히스토리를 생성한다.")
    @Test
    void delete() {
        final List<DeleteHistory> deleteHistories = question.delete();
        assertThat(deleteHistories.get(0)).isEqualTo(new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now()));
        assertThat(deleteHistories.get(1)).isEqualTo(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
    }
}
