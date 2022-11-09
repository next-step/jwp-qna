package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnswersTest {

    @DisplayName("Answers에 Answer을 추가할 수 있다.")
    @Test
    void add() {
        Answers answers = new Answers();
        Answer answer = new Answer();

        answers.add(answer);

        Assertions.assertThat(answers.contains(answer)).isTrue();
    }

    @DisplayName("Answers에 Answer을 제거할 수 있다.")
    @Test
    void remove() {
        Answers answers = new Answers();
        Answer answer = new Answer();
        answers.add(answer);

        answers.remove(answer);

        Assertions.assertThat(answers.isEmpty()).isTrue();
    }

    @DisplayName("Answers의 delete를 수행하면 DeleteHistories를 반환한다.")
    @Test
    void delete() {
        Answers answers = new Answers();
        Answer answer = new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "contents");
        answers.add(answer);

        DeleteHistories deleteHistories = answers.delete(UserTest.JAVAJIGI);

        Assertions.assertThat(deleteHistories.getUnmodifiableDeleteHistories())
                .containsExactly(DeleteHistory.of(ContentType.ANSWER, 1L, UserTest.JAVAJIGI));
    }
}
