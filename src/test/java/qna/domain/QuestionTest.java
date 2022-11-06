package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자 확인 테스트")
    void owner() {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        Assertions.assertThat(question.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("삭제 여부 true 테스트")
    void delete() {
        Question question = new Question(1L, "title", "contents");
        question.deleted();
        Assertions.assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제 여부 false 테스트")
    void delete2() {
        Question question = new Question(1L, "title", "contents");
        Assertions.assertThat(question.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("equals 테스트 (동등한 경우)")
    void equals1() {
        Question actual = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        Question expected = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("equals 테스트 (동등하지 않은 경우)")
    void equals2() {
        Question actual = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        Question expected = new Question(2L, "title", "contents").writeBy(UserTest.JAVAJIGI);

        Assertions.assertThat(actual).isNotEqualTo(expected);
    }


    @Test
    @DisplayName("로그인 사용자와 질문자가 같은 경우 삭제 가능")
    void deleteQuestion1() throws CannotDeleteException {
        Q1.delete(UserTest.JAVAJIGI);
        Assertions.assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자와 질문자가 다른 경우 삭제 불가 (예외발생)")
    void deleteQuestion2() {
        Assertions.assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("로그인 사용자와 질문자가 같으면서, 답변이 없는 경우 삭제 가능")
    void deleteQuestion3() throws CannotDeleteException {
        Q1.delete(UserTest.JAVAJIGI);
        Assertions.assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("로그인 사용자가와 질문자가 같으면서, 답변이 있는데 질문자와 답변자가 하나라도 다른 경우 삭제 불가 (예외발생)")
    void deleteQuestion4() {
        Q1.addAnswer(AnswerTest.A1);
        Q1.addAnswer(AnswerTest.A2);

        Assertions.assertThatThrownBy(() -> Q1.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("로그인 사용자가와 질문자가 같으면서, 답변이 있는데 질문자와 답변자가 모두 같은 경우 삭제 가능")
    void deleteQuestion5() throws CannotDeleteException {
        Question question = new Question(1L, "title", "contents").writeBy(UserTest.JAVAJIGI);
        question.addAnswer(new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));
        question.addAnswer(new Answer(2L, UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));

        question.delete(UserTest.JAVAJIGI);

        Assertions.assertThat(question.isDeleted()).isTrue();
    }
}
