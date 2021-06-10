package qna.domain;

import org.hibernate.sql.Delete;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("삭제 테스트")
    public void delete() throws CannotDeleteException {

        //when
        DeleteHistory deleteHistory = A1.delete(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> A1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);

        //then
        assertThat(A1.isDeleted()).isTrue();
        assertThat(deleteHistory.getDeleteUser()).isEqualTo(UserTest.JAVAJIGI);
    }

    @DisplayName("Answer 생성")
    @Test
    public void newAnswer(){
        assertThat(A1.getWriter()).isEqualTo(UserTest.JAVAJIGI);
        assertThat(A1.getQuestion()).isEqualTo(QuestionTest.Q1);
        assertThat(A1.getContents()).isEqualTo("Answers Contents1");
    }

    @DisplayName("Answer 생성시 User Null 일때 UnAuthorizedException 예외 발생")
    @Test
    public void newAnswerWithUserNUll(){
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents1"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("Answer 생성시 Question Null 일때 NotFoundException 예외 발생")
    @Test
    public void newAnswerWithQuestionNUll(){
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents1"))
                .isInstanceOf(NotFoundException.class);
    }
}
