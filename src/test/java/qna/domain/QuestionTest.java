package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.exceptions.CannotDeleteException;
import qna.exceptions.EmptyStringException;
import qna.exceptions.NullStringException;
import qna.exceptions.StringTooLongException;

@DataJpaTest
public class QuestionTest {

    private static final String TEXT_LENGTH_111 = "Length = 111. This value is too long for the column of this entity. Could not execute the insert SQL statement.";

    private static final User alice = new User(1L, "alice", "password", "Alice", "alice@mail");
    private static final User trudy = new User(2L, "trudy", "123456", "Trudy", "trudy@mail");

    @DisplayName("글쓴이 확인")
    @Test
    void isOwner() {
        User javajigi = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        User sanjigi = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        Question question = new Question("title1", "contents1").writeBy(javajigi);

        assertThat(question.isOwner(javajigi)).isTrue();
        assertThat(question.isOwner(sanjigi)).isFalse();
    }

    @DisplayName("Null 문자열 저장 불가")
    @Test
    void create_NullString_ExceptionThrown() {
        assertThatExceptionOfType(NullStringException.class).isThrownBy(() ->
            new Question(null, "contents")
        );
    }

    @DisplayName("빈 문자열 저장 불가")
    @Test
    void create_EmptyString_ExceptionThrown() {
        assertThatExceptionOfType(EmptyStringException.class).isThrownBy(() ->
            new Question("", "contents")
        );
    }

    @DisplayName("문자열 길이 제한")
    @Test
    void create_StringTooLong_ExceptionThrown() {
        assertThatExceptionOfType(StringTooLongException.class).isThrownBy(() ->
            new Question(TEXT_LENGTH_111, "contents")
        );
    }

    @DisplayName("답변 0 질문 삭제")
    @Test
    void delete_EmtpyAnswers_success() {
        Question question = new Question(1L, "title", "contents").writeBy(alice);

        DeleteHistories deleteHistories = question.delete(alice);

        assertThat(deleteHistories.size()).isEqualTo(1);
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(question, alice))).isTrue();
    }

    @DisplayName("아직 삭제되지 않은 모든 질문 삭제")
    @Test
    void delete_UndeletedAnswers_success() {
        Question question = new Question("title", "contents").writeBy(alice);
        Answers answers = new Answers();
        Answer aliceAnswer1 = new Answer(1L, alice, question, "Alice Answer 1");
        Answer aliceAnswer2 = new Answer(2L, alice, question, "Alice Answer 2");
        Answer othersDeletedAnswer = new Answer(3L, trudy, question, "Trudy Deleted Answer");
        Answer aliceDeletedAnswer = new Answer(4L, alice, question, "Alice Deleted Answer");
        othersDeletedAnswer.delete();
        aliceDeletedAnswer.delete();
        answers.add(aliceAnswer1);
        answers.add(aliceAnswer2);
        answers.add(othersDeletedAnswer);
        answers.add(aliceDeletedAnswer);

        DeleteHistories deleteHistories = question.delete(alice);

        assertThat(deleteHistories.size()).isEqualTo(3);
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(aliceAnswer1, alice))).isTrue();
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(aliceAnswer2, alice))).isTrue();
        assertThat(deleteHistories.hasDeleteHistory(new DeleteHistory(question, alice))).isTrue();
    }

    @DisplayName("다른 사람 답변이 있어 질문 삭제 실패")
    @Test
    void delete_HasOthersAnswer_ExceptionThrown() {
        Question question = new Question("title", "contents").writeBy(alice);
        Answers answers = new Answers();
        answers.add(new Answer(1L, alice, question, "Alice Answer"));
        answers.add(new Answer(2L, trudy, question, "Trudy Answer"));

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() ->
            question.delete(alice)
        ).withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("다른 사람 질문은 삭제 실패")
    @Test
    void delete_NotOwner_ExceptionThrown() {
        Question question = new Question("title", "contents").writeBy(alice);

        assertThatExceptionOfType(CannotDeleteException.class).isThrownBy(() ->
            question.delete(trudy)
        ).withMessage("질문을 삭제할 권한이 없습니다.");
    }

}
