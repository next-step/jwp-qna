package qna.question;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.answer.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.answer.AnswerTest.A1;
import static qna.answer.AnswerTest.A2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", JAVAJIGI);

    @ParameterizedTest
    @ValueSource(strings = {
            "t", "titletitletitletitletitletitletitletitletitletitle" +
            "titletitletitletitletitletitletitletitletitletitle"
    })
    @DisplayName("질문 생성")
    public void createQuestionTest(String title) {
        Question actual = new Question(title, "contents1", JAVAJIGI);
        assertThat(actual).isEqualTo(new Question(title, "contents1", JAVAJIGI));
    }

    @Test
    @DisplayName("질문 생성 실패 - null user")
    public void createQuestionTest_nullUser() {
        assertThatThrownBy(() -> new Question("title1", "contents1", null)).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("질문 생성 실패 - null title")
    public void createQuestionTest_nullTitle() {
        assertThatThrownBy(() -> new Question(null, "contents1", JAVAJIGI)).isInstanceOf(NotFoundException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaa" +
            "aaaaaaaaaaaaaaaaaaaaa"
    })
    @DisplayName("질문 생성 실패 - title의 길이는 0보다크고 100보다 작아야한다.")
    public void createQuestionTest_nullTitle(String title) {
        assertThatThrownBy(() -> new Question(title, "contents1", JAVAJIGI)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("삭제할 수 있는 사용자인지 확인 - 예외 발생")
    public void invalidDeletedUserTest() {
        assertThatThrownBy(() -> Q1.throwExceptionNotDeletableUser(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("삭제할 수 있는 사용자라면 예외 발생 안함")
    public void deletableUserTest() {
        Assertions.assertDoesNotThrow(() -> Q1.throwExceptionNotDeletableUser(JAVAJIGI));
    }

    @Test
    @DisplayName("질문 삭제 상태로 변경")
    public void deleteQuestionTest() {
        //given
        Question actual = new Question("title2", "contents2", SANJIGI);
        assertThat(actual.isDeleted()).isFalse();
        //when
        actual.deleteQuestion();
        //then
        assertThat(actual.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문에 답변 추가 테스트")
    public void addAnswerTest() {
        //given
        Question actual = new Question("title2", "contents2", SANJIGI);
        assertThat(actual.getAnswers()).hasSize(0);
        //when
        actual.addAnswer(A1);
        //then
        assertThat(actual.getAnswers()).contains(A1).hasSize(1);
    }

    @Test
    @DisplayName("다른 사용자가 작성한 답변이 달린 질문 삭제시 예외 발생")
    public void invalidDeletedAnswersUserTest() {
        //given
        Question actual = new Question("title2", "contents2", SANJIGI);
        //when
        actual.addAnswer(A1);
        actual.addAnswer(A2);
        //then
        assertThatThrownBy(() -> actual.throwExceptionNotDeletableAnswers(SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문에 달린 답변 삭제 상태로 변경")
    public void deleteAnswersInQuestionTest() {
        //given
        Question actual = new Question("title2", "contents2", SANJIGI);
        //when
        actual.addAnswer(A2);
        actual.deleteAnswers();
        //then
        assertThat(actual.getAnswers().stream().map(Answer::isDeleted)).doesNotContain(false);
    }


}
