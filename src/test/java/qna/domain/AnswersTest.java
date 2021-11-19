package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    private Answers answers;
    private Question question1;
    private Answer answer1;
    private Answer answer2;

    @BeforeEach
    void setUp() {
        answers = new Answers();

        question1 = new Question("title1", "contents1")
            .writeBy(UserTest.JAVAJIGI);

        answer1 = new Answer(UserTest.JAVAJIGI, question1, "답변1 입니다.");
        answer2 = new Answer(UserTest.JAVAJIGI, question1, "답변2 입니다.");
    }

    @Test
    void test_답변_추가() {
        answers.addAnswer(answer1);
        answers.addAnswer(answer2);

        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    void test_모든_답변_삭제() {
        answers.addAnswer(answer1);
        answers.addAnswer(answer2);

        assertAll(
            () -> assertThat(answers.delete(UserTest.JAVAJIGI)).isInstanceOf(
                DeleteHistories.class),
            () -> assertThat(answers.delete(UserTest.JAVAJIGI).size()).isEqualTo(2),
            () -> assertThat(answers.answers().stream().map(Answer::isDeleted).count()).isEqualTo(2)
        );
    }

    @Test
    void test_다른_사람_답변_삭제시도시_예외() {
        answers.addAnswer(answer1);
        answers.addAnswer(answer2);

        assertThatThrownBy(() -> answers.delete(UserTest.SANJIGI)).
            isInstanceOf(CannotDeleteException.class);
    }
}