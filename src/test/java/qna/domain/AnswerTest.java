package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {

    @DisplayName("Answer 생성시 User가 없으면 UnAuthorizedException 이 발생한다.")
    @Test
    void constructor_user() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "content"))
                .isInstanceOf(UnAuthorizedException.class);
    }


    @DisplayName("Answer 생성시 Question이 없으면 NotFoundException 이 발생한다.")
    @Test
    void constructor_question() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "content"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Answer의 작성자를 확인할 수 있다.")
    @Test
    void is_owner() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "content");

        assertAll(
                () -> assertThat(answer.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(answer.isOwner(UserTest.SANJIGI)).isFalse()
        );

    }

    @DisplayName("Answer의 Question을 변경할 수 있다.")
    @Test
    void to_question() {
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "content");

        answer.toQuestion(QuestionTest.Q2);

        assertThat(answer.getQuestion()).isEqualTo(QuestionTest.Q2);
    }

    @DisplayName("Answer의 Question를 변경하면 연과관계 Qustion의 answers에도 변경 반영된다.")
    @Test
    void mapping_question() {
        Question question1 = new Question("title1", "contents2");
        Question question2 = new Question("title2", "contents2");
        Answer answer = new Answer(UserTest.JAVAJIGI, question1, "answer");

        answer.toQuestion(question2);

        Assertions.assertAll(
                () -> assertThat(answer.getQuestion()).isEqualTo(question2),
                () -> assertThat(question1.getAnswers()).isEmpty(),
                () -> assertThat(question2.getAnswers()).containsExactly(answer)
        );
    }
}
