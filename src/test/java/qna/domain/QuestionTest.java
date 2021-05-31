package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    Question question;
    Answer answer1;
    Answer answer2;

    @BeforeEach
    void setUp() {
        question = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        answer1 = new Answer(UserTest.JAVAJIGI, question, "Answers Contents1");
        answer2 = new Answer(UserTest.SANJIGI, question, "Answers Contents2");
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 삭제시 예외가 발생한다")
    void deleteNotOwnerExceptionTest() {
        assertThatThrownBy(() -> question.deleteByOwner(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제 시 deleted : true 변경, contentType : Question 인 DeleteHistory 를 반환한다")
    void deleteQuestionTest() throws CannotDeleteException {
        DeleteHistory deleteHistory = question.deleteByOwner(question.getWriter());

        assertThat(question.isDeleted()).isEqualTo(true);
        assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION);
    }

    @Test
    @DisplayName("삭제 시 매핑된 answers 도 삭제된다")
    void deleteAnswersTest() throws CannotDeleteException {
        question.addAnswer(answer1);
        question.deleteByOwner(question.getWriter());

        assertThat(answer1.isDeleted()).isEqualTo(true);
    }

    @Test
    @DisplayName("삭제 시 다른 사람이 작성한 Answer 가 있는 경우 예외가 발생한다")
    void deleteAnswersContainsNotOwnerExceptionTest() {
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        assertThat(answer1.getWriter()).isNotSameAs(answer2.getWriter());
        assertThatThrownBy(() -> question.deleteByOwner(question.getWriter()))
                .isInstanceOf(CannotDeleteException.class);
    }
}
