package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("writeBy의 결과 입력한 작성자 정보를 저장한 객체를 반환한다")
    void writeBy() {
        // given
        final Question question = new Question(3L, "title3", "contents3");
        User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        Long expected = question.getWriterId();

        // when
        final Question question2 = question.writeBy(user);
        Long actual = question2.getWriterId();

        // then
        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    @DisplayName("isOwner의 결과 입력된 유저 정보와 현재 저장된 유저 정보가 일치하는지 여부를 반환한다")
    void isOwner() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question("title3", "contents3").writeBy(user);

        // when
        final boolean actual = question.isOwner(user);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("addAnswer의 결과 Answer 객체의 Question ID를 변경한다")
    void addAnswer() {
        // given
        final User user = new User(3L, "mins99", "1234", "ms", "mins99@slipp.net");
        final Question question = new Question(3L, "title3", "contents3");
        final Question question2 = new Question(4L, "title4", "contents4");
        final Answer answer = new Answer(user, question, "Answers Contents3");
        final long expected = answer.getQuestionId();

        // when
        question2.addAnswer(answer);
        final long actual = answer.getQuestionId();

        // then
        assertThat(actual).isNotEqualTo(expected);
    }
}
