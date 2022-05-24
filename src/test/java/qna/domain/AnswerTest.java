package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("isOwner의 결과 입력받은 유저 정보가 실제 answer 객체의 유저 정보를 비교하여 참/거짓을 반환한다.")
    void isOwner() {
        // given & when & then
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse(),
                () -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.JAVAJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("toQuestion의 결과 answer 객체의 questionId 값이 변경된다.")
    void toQuestion() {
        // given
        User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        Question question = new Question(3L, "title3", "contents3");
        Question question2 = new Question(4L, "title4", "contents4");
        Answer answer = new Answer(3L, user, question, "Answers Contents3");
        long expected = answer.getQuestion().getId();

        // when
        answer.toQuestion(question2);
        long actual = answer.getQuestion().getId();

        // then
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    @DisplayName("delete의 결과 answer 객체가 삭제 상태로 값이 변경된다.")
    void delete() {
        // given
        User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        Question question = new Question(3L, "title3", "contents3");
        Answer answer = new Answer(3L, user, question, "Answers Contents3");

        // when
        answer.delete();
        boolean actual = answer.isDeleted();

        assertThat(actual).isTrue();
    }
}
