package qna.domain;

import static java.time.LocalDateTime.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.ContentType.*;
import static qna.domain.UserTest.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;
import qna.TestUtils;
import qna.domain.vo.Title;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", SANJIGI);

    @Test
    @DisplayName("처음 질문을 만들면 삭제 상태가 아니다")
    void deletedTest() {
        Question question = new Question("title", "contents", JAVAJIGI);

        assertThat(question.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("질문의 제목은 100자가 넘어가면 안된다.")
    void titleTest() {
        String maxLengthTitle = TestUtils.createText(100);
        String overMaxLengthTitle = TestUtils.createText(101);
        Question question = new Question(maxLengthTitle, "content", JAVAJIGI);

        assertThat(question.getTitle()).isEqualTo(Title.of(maxLengthTitle));
        assertThatThrownBy(() -> new Question(overMaxLengthTitle, "content", JAVAJIGI))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("질문에 답변이 없으면 질문을 삭제 가능하다, 질문이 삭제 상태되고, 삭제 내역이 반환된다.")
    void deleteStatusTest() throws Exception {
        Question question = new Question(1L, "to be deleted", "contents", JAVAJIGI);
        DeleteHistory questionDeleteHistory = new DeleteHistory(QUESTION, 1L, JAVAJIGI, now());

        verifyDeleteQuestion(question, DeleteHistories.of(questionDeleteHistory));
    }

    @Test
    @DisplayName("질문에 질문자의 답변만 있을 경우 삭제 가능하다")
    void canDeleteOnlyMyQuestion() throws Exception {
        Question question = new Question(1L, "to be deleted", "contents", JAVAJIGI);
        Answer myAnswer = new Answer(1L, JAVAJIGI, question, "answer contents");

        DeleteHistories expectedDeleteHistories = DeleteHistories.of(
            new DeleteHistory(QUESTION, 1L, JAVAJIGI, now()),
            new DeleteHistory(ANSWER, 1L, JAVAJIGI, now())
        );

        verifyDeleteQuestion(question, expectedDeleteHistories);
    }

    @Test
    @DisplayName("질문자만 질문을 삭제할 수 있다.")
    void deleteAuthorizationTest() {
        Question question = new Question("to be deleted", "contents", JAVAJIGI);

        assertThatThrownBy(() -> question.delete(SANJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 다른 사람 답변이 있으면 삭제할 수 없다.")
    void canNotDeleteTest() {
        Question question = new Question("to be deleted", "contents", JAVAJIGI);
        Answer answer = new Answer(SANJIGI, question, "answer contents");

        assertThatThrownBy(() -> question.delete(JAVAJIGI))
            .isInstanceOf(CannotDeleteException.class);
    }

    private void verifyDeleteQuestion(Question question, DeleteHistories expectedDeleteHistories) throws CannotDeleteException {
        DeleteHistories actualHistories = question.delete(JAVAJIGI);
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(actualHistories).isEqualTo(expectedDeleteHistories),
            () -> assertThat(question.isAnswersDeleted()).isTrue()
        );
    }
}
